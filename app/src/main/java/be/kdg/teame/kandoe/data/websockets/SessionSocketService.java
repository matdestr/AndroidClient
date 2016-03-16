package be.kdg.teame.kandoe.data.websockets;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.data.websockets.stomp.SubscriptionCallback;
import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;
import be.kdg.teame.kandoe.data.websockets.stomp.Subscription;
import be.kdg.teame.kandoe.di.Injector;

public class SessionSocketService implements Runnable{
    private String destination;
    private SubscriptionCallback subscriptionCallback;
    private Stomp stomp;
    private boolean run;

    private final static long INTERVAL = 500L;
    private final static String SUBSCRIPTION_ID = "subscription_id_session_service";

    public SessionSocketService(@NonNull String destination, @NonNull SubscriptionCallback subscriptionCallback) {
        this.destination = destination;
        this.subscriptionCallback = subscriptionCallback;
        this.stomp = Injector.getStomp();
        this.run = true;
    }

    @Override
    public void run() {
        stomp.connect();

        Subscription subscription = new Subscription(destination, subscriptionCallback);
        subscription.setId(SUBSCRIPTION_ID);

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
