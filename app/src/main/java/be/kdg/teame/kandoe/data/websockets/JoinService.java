package be.kdg.teame.kandoe.data.websockets;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

import be.kdg.teame.kandoe.data.websockets.stomp.SubscriptionCallback;
import be.kdg.teame.kandoe.data.websockets.stomp.ListenerWSNetwork;
import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;
import be.kdg.teame.kandoe.data.websockets.stomp.Subscription;
import be.kdg.teame.kandoe.di.Injector;

public class JoinService implements Runnable {
    private String destination;
    private SubscriptionCallback subscriptionCallback;
    private Stomp stomp;

    public JoinService(@NonNull String destination, @NonNull SubscriptionCallback subscriptionCallback){

        this.destination = destination;
        this.subscriptionCallback = subscriptionCallback;

        this.stomp = new Stomp(Injector.getWebSocketBaseUrl().concat("/ws"),
                new HashMap<String, String>(),
                new ListenerWSNetwork() {
            @Override
            public void onState(int state) {
                switch (state){
                    case Stomp.CONNECTED:
                        Log.i("Stomp-state", "Connected");
                        break;

                    case Stomp.DISCONNECTED_FROM_APP:
                        Log.i("Stomp-state", "Deconnected from app");
                        break;

                    case Stomp.DISCONNECTED_FROM_OTHER:
                        Log.i("Stomp-state", "Deconnected from app");
                        break;

                    case Stomp.NOT_AGAIN_CONNECTED:
                        Log.i("Stomp-state", "Not again connected");
                        break;

                    default:
                        Log.i("Stomp-state", "Unkown stomp state: " + state);
                }
            }
        });
    }

    @Override
    public void run() {
        stomp.connect();
        Log.i(this.getClass().getSimpleName(), "Stomp connected");

        stomp.subscribe(new Subscription(destination, subscriptionCallback));
    }
}
