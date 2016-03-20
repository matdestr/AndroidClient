package be.kdg.teame.kandoe.di;

import java.util.HashMap;

import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.data.websockets.stomp.ListenerWSNetwork;
import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;
import lombok.Getter;

/**
 * Global injector which contains basic information that can be used on multiple locations.
 */
public final class Injector {
    @Getter
    private static Class unauthenticatedRedirectActivity = SignInActivity.class;

    @Getter
    private static String clientDetailsHeader = "Basic YW5kcm9pZDpzZWNyZXQ=";

    /**
     * Holds the api base url for connecting to the server.
     * <p/>
     * The api base url for the localhost is "http://10.0.3.2:8080/kandoe".
     * The api base url for the wildfly-server is "https://wildfly-teameip2kdgbe.rhcloud.com"
     */
    @Getter
    private static final String apiBaseUrl = "http://10.0.3.2:8080/kandoe";

    /**
     * Holds the web sockets base url for connecting to the server.
     * <p/>
     * The web sockets base url for the localhost is "http://10.0.3.2:8080/kandoe".
     * The web sockets base url for the wildfly-server is "ws://wildfly-teameip2kdgbe.rhcloud.com"
     */
    @Getter
    private static final String webSocketBaseUrl = "ws://10.0.3.2:8080/kandoe";

    @Getter
    private static final String threadPrefix = "Thread_";

    @Getter
    private static final Stomp stomp =
            new Stomp(
                    Injector.getWebSocketBaseUrl().concat("/ws"),
                    new HashMap<String, String>(),
                    new ListenerWSNetwork() {
                        @Override
                        public void onState(int state) {
                            switch (state) {
                                case Stomp.CONNECTED:
                                    break;

                                case Stomp.DISCONNECTED_FROM_APP:
                                    break;

                                case Stomp.DISCONNECTED_FROM_OTHER:
                                    break;

                                case Stomp.NOT_AGAIN_CONNECTED:
                                    break;
                            }
                        }
                    });

}
