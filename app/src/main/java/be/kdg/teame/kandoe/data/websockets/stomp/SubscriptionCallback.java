package be.kdg.teame.kandoe.data.websockets.stomp;

import java.util.Map;

/**
 * Callback function for handling STOMP subscriptions
 * https://stackoverflow.com/questions/24346068/set-up-a-stomp-client-in-android-with-spring-framework-in-server-side
 */

public interface SubscriptionCallback {
    void onMessage(Map<String, String> headers, String body);
}