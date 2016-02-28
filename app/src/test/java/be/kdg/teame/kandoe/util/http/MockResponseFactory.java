package be.kdg.teame.kandoe.util.http;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class MockResponseFactory {
    private static final String URL = "mock-url";
    private static final String DEFAULT_REASON = "Mock reason";

    public static Response getMockResponse(int statusCode) {
        return getMockResponse(statusCode, DEFAULT_REASON);
    }

    public static Response getMockResponse(int statusCode, String reason) {
        return new Response(URL, statusCode, reason, new ArrayList<Header>(), null);
    }

    public static Response getMockResponse(int statuscode, String reason, TypedInput body) throws JSONException {
        //return new Response(URL, statuscode, reason, new ArrayList<Header>(), new TypedString(bodyJson));
        return new Response(URL, statuscode, reason, new ArrayList<Header>(), body);
    }
}
