package be.kdg.teame.kandoe.data.websockets;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.data.websockets.stomp.SubscriptionCallback;
import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;
import be.kdg.teame.kandoe.data.websockets.stomp.Subscription;
import be.kdg.teame.kandoe.di.Injector;
import lombok.Getter;

/**
 * Service that is responsible for one web socket connection,
 * using the {@link Stomp} protocol over web sockets.
 * It contains the destination of the web socket, its subscription id and callback.
 * When starting this service a stomp connection is opened and subscribes on the delivered destination.
 * When stopping this service unsubscribes from its destination and the stomp connection disconnects properly.
 *
 * @see Runnable
 */
public class SocketService implements Runnable{
    @Getter
    private String subscriptionId;
    private String destination;
    private SubscriptionCallback subscriptionCallback;
    private Stomp stomp;
    private boolean run;

    private final static long INTERVAL = 500L;

    public SocketService(@NonNull String destination, @NonNull String subscriptionId, @NonNull SubscriptionCallback subscriptionCallback) {
        this.subscriptionId = subscriptionId;
        this.destination = destination;
        this.subscriptionCallback = subscriptionCallback;
        this.stomp = Injector.getStomp();
        this.run = true;
    }

    @Override
    public void run() {
        stomp.connect();

        Subscription subscription = new Subscription(destination, subscriptionCallback);
        subscription.setId(subscriptionId);

        stomp.subscribe(subscription);

        while (run){
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                // do nothing
            }
        }

        stomp.unsubscribe(subscription.getId());
        stomp.disconnect();
    }

    public void stop(){
        this.run = false;
    }
}
