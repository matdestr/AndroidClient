package be.kdg.teame.kandoe.session;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Map;

import javax.inject.Inject;

import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.data.websockets.SessionSocketService;
import be.kdg.teame.kandoe.data.websockets.WebSocketsManager;
import be.kdg.teame.kandoe.data.websockets.stomp.SubscriptionCallback;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.models.sessions.SessionStatus;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionPresenter implements SessionContract.UserActionsListener {
    private final SessionService mSessionService;
    private SessionContract.View mSessionView;

    @Inject
    public SessionPresenter(SessionService sessionService, PrefManager prefManager){
        this.mSessionService = sessionService;
    }

    @Override
    public void setView(@NonNull SessionContract.View view) {
        mSessionView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {

    }

    @Override
    public void startListening(int sessionId) {
        final SessionSocketService sessionSocketService = new SessionSocketService(
                String.format("/topic/sessions/%d/status", sessionId),
                new SubscriptionCallback() {
                    @Override
                    public void onMessage(Map<String, String> headers, String body) {
                        Log.d("SocketService-message", "session-status:".concat(body));
                        SessionStatus status = SessionStatus.valueOf(body.split("(.*?)", 1)[0]);
                        mSessionView.onSessionStatusChanged(status);
                    }
                });

        Thread thread = WebSocketsManager.getThread(sessionSocketService, sessionId);

        if (!thread.isAlive())
            thread.start();
    }

    @Override
    public void loadSession(final int sessionId) {
        mSessionService.getSession(sessionId, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                mSessionView.showSession(session);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null && error.getResponse() != null) {
                    if (error.getResponse().getStatus() == HttpStatus.BAD_REQUEST || error.getResponse().getStatus() == HttpStatus.UNAUTHORIZED) {
                        mSessionView.launchUnauthenticatedRedirectActivity();
                    } else {
                        mSessionView.showErrorConnectionFailure("Unable to retrieve information for session.");
                    }
                } else {
                    mSessionView.showErrorConnectionFailure(null);
                }
            }
        });
    }
}
