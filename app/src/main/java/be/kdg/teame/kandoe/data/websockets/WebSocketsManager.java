package be.kdg.teame.kandoe.data.websockets;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages threads for web sockets. It handles incoming requests for threads and
 * generates a new {@link Thread} if there isn't already one registered before.
 * Otherwise the registered {@link Thread} will be returned.
 * A unique {@link Thread} is formed by using a {@link SocketService} and an identifier of a generic type.
 *
 * @see SocketService
 */
public class WebSocketsManager {
    private static Set<Thread> threads = new HashSet<>();

    private WebSocketsManager() {
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
     * @return a new thread or an existing thread
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
}
