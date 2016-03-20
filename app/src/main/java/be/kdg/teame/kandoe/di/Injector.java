package be.kdg.teame.kandoe.di;

import android.util.Log;

import java.util.HashMap;

import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.data.websockets.stomp.ListenerWSNetwork;
import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;
import lombok.Getter;

public final class Injector {
    @Getter
    private static Class unauthenticatedRedirectActivity = SignInActivity.class;

    @Getter
    private static String clientDetailsHeader = "Basic YW5kcm9pZDpzZWNyZXQ=";

    @Getter
    private static final String apiBaseUrl = /*"https://wildfly-teameip2kdgbe.rhcloud.com"*/ "http://10.0.3.2:8080/kandoe";

    @Getter
    private static final String webSocketBaseUrl =/* "ws://wildfly-teameip2kdgbe.rhcloud.com"*/ "ws://10.0.3.2:8080/kandoe";

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
                                    //Log.i("Stomp-state", "Connected");
                                    break;

                                case Stomp.DISCONNECTED_FROM_APP:
                                    //Log.i("Stomp-state", "Disconnected from app");
                                    break;

                                case Stomp.DISCONNECTED_FROM_OTHER:
                                    //Log.i("Stomp-state", "Disconnected from app");
                                    break;

                                case Stomp.NOT_AGAIN_CONNECTED:
                                    //Log.i("Stomp-state", "Not again connected");
                                    break;

                                default:
                                    //Log.i("Stomp-state", "Unknown stomp state: " + state);
                            }
                        }
                    });

}
