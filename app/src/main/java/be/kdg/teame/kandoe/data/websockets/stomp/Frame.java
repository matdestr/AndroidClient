package be.kdg.teame.kandoe.data.websockets.stomp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frame {
//  private final static String CONTENT_LENGTH = "content-length";

    private String command;
    private Map<String, String> headers;
    private String body;

    /**
     * Constructor of a Frame object. All parameters of a frame can be instantiate
     *
     * @param command
     * @param headers
     * @param body
     */
    public Frame(String command, Map<String, String> headers, String body) {
        this.command = command;
        this.headers = headers != null ? headers : new HashMap<String, String>();
        this.body = body != null ? body : "";
    }

    public String getCommand() {
        return command;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    /**
     * Create a frame from a received message. This method is copied on the objective C one, in the MMPReactiveStompClient
     * library
     *
     * @param data a part of the message received from network, which represented a frame
     * @return An object frame
     */
    public static Frame toFrame(String data) {
        Log.wtf("frame-data", data);
        List<String> contents = new ArrayList<>(Arrays.asList(data.split(Byte.LF)));

        while (contents.size() > 0 && contents.get(0).isEmpty()) {
            contents.remove(0);
        }

        String command = contents.get(0);
        Map<String, String> headers = new HashMap<>();
        String body = "";

        contents.remove(0);
        boolean hasHeaders = false;
        for (String line : contents) {
            if (hasHeaders) {
                for (int i = 0; i < line.length(); i++) {
                    Character c = line.charAt(i);
                    if (!c.equals(Byte.NULL))
                        body += c;
                }
            } else {
                if (line.isEmpty()) {
                    hasHeaders = true;
                } else {
                    String[] header = line.split(":");
                    headers.put(header[0], header[1]);
                }
            }
        }
        return new Frame(command, headers, body);
    }

    /**
     * Create a frame with based fame component and convert them into a string
     *
     * @param command
     * @param headers
     * @param body
     * @return a frame object convert in a {@code String}
     */
    public static String marshall(String command, Map<String, String> headers, String body) {
        Frame frame = new Frame(command, headers, body);
        return frame.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.command);
        builder.append(Byte.LF);

        for (String key : this.headers.keySet())
            builder.append(key)
                    .append(":")
                    .append(this.headers.get(key))
                    .append(Byte.LF);


        builder.append(Byte.LF)
                .append(this.body)
                .append(Byte.NULL);

        return builder.toString();
    }

    private class Byte {
        public static final String LF = "\n";
        public static final String NULL = "\0";
    }

}