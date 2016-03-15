package be.kdg.teame.kandoe.session;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionPresenter implements SessionContract.UserActionsListener {
    private final SessionService mSessionService;
    private final PrefManager mPrefManager;

    private SessionContract.View mSessionView;

    @Inject
    public SessionPresenter(SessionService sessionService, PrefManager prefManager){
        mSessionService = sessionService;
        mPrefManager = prefManager;
    }

    @Override
    public void loadSession(final int sessionId) {
        mSessionService.getSession(sessionId, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                Log.d("Session-load", "Session " + sessionId + " retrieved.");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Session-load", "Failed to load session " + sessionId);
            }
        });
    }

    @Override
    public void setView(@NonNull SessionContract.View view) {
        mSessionView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {

    }
}
