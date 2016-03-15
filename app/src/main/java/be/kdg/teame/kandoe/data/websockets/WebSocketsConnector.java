package be.kdg.teame.kandoe.data.websockets;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

import be.kdg.teame.kandoe.data.websockets.stomp.ListenerWSNetwork;
import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;
import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.util.preferences.PrefManager;


public class WebSocketsConnector extends AsyncTask<Void, Void, Stomp> {
    private StompService mStompService;
    private PrefManager mPrefManager;

    public WebSocketsConnector(StompService service, PrefManager prefManager) {
        mStompService = service;
        mPrefManager = prefManager;
    }

    @Override
    protected Stomp doInBackground(Void... params) {
        String uri = Injector.getWebSocketBaseUrl().concat("/ws");

        Stomp stomp = new Stomp(
                uri,
                new HashMap<String, String>(),
                new ListenerWSNetwork() {
                    @Override
                    public void onState(int state) {

                        switch (state) {
                            case Stomp.CONNECTED:
                                Log.i("Stomp-state", String.valueOf(state));
                                break;
                            default:
                                Log.i("Stomp-state", String.valueOf(state));
                        }
                    }
                });

        return stomp;
    }

    @Override
    protected void onPostExecute(Stomp stomp) {
        this.mStompService.onConnected(stomp);
    }
}
