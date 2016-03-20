package be.kdg.teame.kandoe.data.websockets.stomp;

/**
 * Entity that represents a Stomp subscription
 * https://stackoverflow.com/questions/24346068/set-up-a-stomp-client-in-android-with-spring-framework-in-server-side
 */

public class Subscription {

    private String id;

    private String destination;

    private SubscriptionCallback callback;

    public Subscription(String destination, SubscriptionCallback callback){
        this.destination = destination;
        this.callback = callback;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public SubscriptionCallback getCallback() {
        return callback;
    }
}