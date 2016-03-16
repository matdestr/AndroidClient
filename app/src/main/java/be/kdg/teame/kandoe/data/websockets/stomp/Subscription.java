package be.kdg.teame.kandoe.data.websockets.stomp;

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