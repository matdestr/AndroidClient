package be.kdg.teame.kandoe.data.websockets.stomp;

/**
 * Listens for a Websocket connections
 * https://stackoverflow.com/questions/24346068/set-up-a-stomp-client-in-android-with-spring-framework-in-server-side
 */

public interface ListenerWSNetwork {
    void onState(int state);
}