package be.kdg.teame.kandoe.data.websockets;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.data.websockets.stomp.SubscriptionCallback;
import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;
import be.kdg.teame.kandoe.data.websockets.stomp.Subscription;
import be.kdg.teame.kandoe.di.Injector;
import lombok.Getter;

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
