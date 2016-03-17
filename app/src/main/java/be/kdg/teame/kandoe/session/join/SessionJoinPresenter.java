package be.kdg.teame.kandoe.session.join;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.data.websockets.SocketService;
import be.kdg.teame.kandoe.data.websockets.WebSocketsManager;
import be.kdg.teame.kandoe.data.websockets.stomp.SubscriptionCallback;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SessionJoinPresenter implements SessionJoinContract.UserActionsListener {
    private final SessionService mSessionService;
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
                mSessionJoinView.setProgressIndicator(false);
                Log.d("Session-join", "Joined session " + sessionId);
                mSessionJoinView.showJoined();
                startWebSocket(sessionId);
            }

            @Override
            public void failure(RetrofitError error) {
                mSessionJoinView.setProgressIndicator(false);

                String errorMessage = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                try {
                    JSONObject jsonObject = new JSONObject(errorMessage);
                    mSessionJoinView.showErrorConnectionFailure(jsonObject.getString("message"));
                } catch (JSONException e) {
                    Log.d("Session-join", "JSONException: ".concat(e.getMessage()), e);
                    mSessionJoinView.showErrorConnectionFailure("Sorry, something went wrong.");
                }
            }
        });
    }

    @Override
    public void decline(final int sessionId) {
        mSessionService.decline(sessionId, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d("Session-join", String.format("Successfully declined session invite for session %d", sessionId));
                mSessionJoinView.close();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Session-join", "Failure: " + error.getMessage(), error);

                String errorMessage = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                try {
                    JSONObject jsonObject = new JSONObject(errorMessage);
                    mSessionJoinView.showErrorConnectionFailure(jsonObject.getString("message"));
                } catch (JSONException e) {
                    Log.d("Session-join", "JSONException: ".concat(e.getMessage()), e);
                    mSessionJoinView.showErrorConnectionFailure("Sorry, something went wrong.");
                }
            }
        });
    }

    @Override
    public void setView(@NonNull SessionJoinContract.View view) {
        mSessionJoinView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionJoinView);
    }

    private void startWebSocket(int sessionId) {
        SocketService participantsJoinSocketService = new SocketService(
                String.format("/topic/sessions/%d/participants", sessionId),
                "session_join_subscription_id",
                new SubscriptionCallback() {
            @Override
            public void onMessage(Map<String, String> headers, String body) {
                Log.d(SessionJoinPresenter.this.getClass().getSimpleName(), body);
                mSessionJoinView.showUserJoined();
            }
        });

        Thread joinThread = WebSocketsManager.getThread(participantsJoinSocketService, sessionId);

        joinThread.start();
    }
}
