package be.kdg.teame.kandoe.dashboard.sessionlist;

import android.support.annotation.NonNull;

import java.util.List;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This class is responsible for managing and performing the actions that the user can initiate in {@link SessionListFragment}.
 * It implements {@link SessionListContract.UserActionsListener} and notifies the view after completing its actions.
 *
 * @see SessionListContract.UserActionsListener
 * */
public class SessionListPresenter implements SessionListContract.UserActionsListener {
    private SessionListContract.View mSessionListView;
    private final SessionService mSessionService;
    private final PrefManager mPrefManager;

    public SessionListPresenter(SessionService sessionService, PrefManager prefManager) {
        this.mSessionService = sessionService;
        this.mPrefManager = prefManager;
    }

    @Override
    public void loadSessions() {
        mSessionListView.showZeroSessionsFoundMessage(false);
        mSessionListView.setProgressIndicator(true);

        mSessionService.getSessions(new Callback<List<SessionListItem>>() {
            @Override
            public void success(List<SessionListItem> sessions, Response response) {
                mSessionListView.setProgressIndicator(false);

                if (sessions.size() > 0)
                    mSessionListView.showSessions(sessions);
                else
                    mSessionListView.showZeroSessionsFoundMessage(true);
            }

            @Override
            public void failure(RetrofitError error) {
                mSessionListView.setProgressIndicator(false);
                mSessionListView.showErrorConnectionFailure("Oops something went wrong!");
            }
        });
    }

    @Override
    public void openSession(SessionListItem session) {
        mSessionListView.showSessionDetail(session);
    }

    @Override
    public void setView(@NonNull SessionListContract.View view) {
        mSessionListView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionListView);
    }
}
