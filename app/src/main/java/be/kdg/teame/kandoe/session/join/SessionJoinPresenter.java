package be.kdg.teame.kandoe.session.join;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.data.websockets.JoinService;
import be.kdg.teame.kandoe.data.websockets.stomp.ListenerSubscription;
import be.kdg.teame.kandoe.session.SessionActivity;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SessionJoinPresenter implements SessionJoinContract.UserActionsListener {
    private final SessionService mSessionService;
    private JoinService mJoinServiceRunnable;
    private final PrefManager mPrefManager;

    private SessionJoinContract.View mSessionJoinView;

    @Inject
    public SessionJoinPresenter(SessionService sessionService,PrefManager prefManager) {
        this.mSessionService = sessionService;
        this.mPrefManager = prefManager;
    }

    @Override
    public void join(final int sessionId) {
        mSessionJoinView.setProgressIndicator(true);
        mSessionService.join(sessionId, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d("Session-join", "Joined session " + sessionId);
                startWebSocket(sessionId);
            }

            @Override
            public void failure(RetrofitError error) {
                String errorMessage = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                try {
                    JSONObject jsonObject = new JSONObject(errorMessage);
                    mSessionJoinView.showError(jsonObject.getString("message"));
                } catch (JSONException e) {
                    Log.d("Session-join", "JSONException: ".concat(e.getMessage()), e);
                    mSessionJoinView.showError("Sorry, something went wrong.");
                }
            }
        });
    }

    @Override
    public void decline(int sessionId) {

    }

    @Override
    public void setView(@NonNull SessionJoinContract.View view) {
        mSessionJoinView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionJoinView);
    }

    private void startWebSocket(int sessionId){
        mJoinServiceRunnable = new JoinService(String.format("/topic/sessions/%d/participants", sessionId), new ListenerSubscription() {
            @Override
            public void onMessage(Map<String, String> headers, String body) {
                Log.d("Session-join", body);
            }
        });

        new Thread(mJoinServiceRunnable).start();
    }
}
