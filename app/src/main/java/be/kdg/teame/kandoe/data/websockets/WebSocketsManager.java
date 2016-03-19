package be.kdg.teame.kandoe.data.websockets;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class WebSocketsManager {
    private static Set<Thread> threads = new HashSet<>();

    private WebSocketsManager() {

    }

    private static String generateUniqueThreadName(SocketService service) {
        final String prefix = "Thread_";

        return prefix
                .concat(service.getClass().getSimpleName().replaceAll(" ", "_"))
                .concat("_")
                .concat(service.getSubscriptionId().replaceAll(" ", "_"));
    }

    private static <T> String generateUniqueThreadName(SocketService service, T identifier) {
        final String prefix = "Thread_";

        return prefix
                .concat(service.getClass().getSimpleName().replaceAll(" ", "_"))
                .concat("_")
                .concat(service.getSubscriptionId().replaceAll(" ", "_"))
                .concat("_")
                .concat(String.valueOf(identifier).replaceAll(" ", "_"));

    }

    /**
     * Registers the thread in the {@link WebSocketsManager} if the thread not exists.
     * If the thread exists it will return the existing thread.
     *
     * @return the existing thread
     */
    public static <T> Thread getThread(SocketService service, T identifier) {
        String name = generateUniqueThreadName(service, identifier);

        Thread thread = null;

        for (Thread existingThread : threads) {
            if (name.equals(existingThread.getName())) {
                Log.i(WebSocketsManager.class.getSimpleName(), "thread already exists");
                thread = existingThread;
                break;
            }
        }

        if (thread == null){
            thread = new Thread(service, name);
            threads.add(thread);
        }

        return thread;
    }


    /**
     * Registers the thread in the {@link WebSocketsManager} if the thread not exists.
     * If the thread exists it will return the existing thread.
     *
     * @return the existing thread
     */
    public static Thread getThread(SocketService service) {
        String name = generateUniqueThreadName(service);

        for (Thread existingThread : threads)
            if (name.equals(existingThread.getName()))
                return existingThread;


        Thread thread = new Thread(service, name);

        threads.add(thread);

        return thread;

    }
}
