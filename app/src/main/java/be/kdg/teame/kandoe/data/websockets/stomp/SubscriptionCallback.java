package be.kdg.teame.kandoe.data.websockets.stomp;

import java.util.Map;
public interface SubscriptionCallback {
    public void onMessage(Map<String, String> headers, String body);
}