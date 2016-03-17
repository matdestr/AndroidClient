package be.kdg.teame.kandoe.data.websockets;

import java.util.HashSet;
import java.util.Set;

public class WebSocketsManager {
    private static Set<Thread> threads = new HashSet<>();

    private WebSocketsManager(){

    }

    /**
     * Generates a unique identifier for a Thread.
     *
     * @return the unique identifier
     * */
    private static <T> String generateUniqueThreadName(Runnable runnable, T identifier) {
        final String prefix = "Thread_";

        return prefix
                .concat(runnable.getClass().getSimpleName().replaceAll(" ", "_"))
                .concat("_")
                .concat(String.valueOf(identifier).replaceAll(" ", "_"));

    }

    /**
     * Registers the thread in the {@link WebSocketsManager} if the thread does not exist.
     * If the thread exists it will return the existing thread.
     *
     * @return the existing thread
     */
    public static <T> Thread getThread(Runnable runnable, T identifier) {
        String name = generateUniqueThreadName(runnable, identifier);

        for (Thread existingThread : threads)
            if (name.equals(existingThread.getName()))
                return existingThread;


        Thread thread = new Thread(runnable, name);

        threads.add(thread);

        return thread;

    }
}
