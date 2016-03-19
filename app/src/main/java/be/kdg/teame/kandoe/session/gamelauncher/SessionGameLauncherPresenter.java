package be.kdg.teame.kandoe.session.gamelauncher;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionGameLauncherPresenter implements SessionGameLauncherContract.UserActionsListener {

    private SessionGameLauncherContract.View mSessionGameLauncherView;

    private final SessionService mSessionService;
    private final PrefManager mPrefManager;

    @Inject
    public SessionGameLauncherPresenter(SessionService sessionService, PrefManager prefManager) {
        mSessionService = sessionService;
        mPrefManager = prefManager;
    }

    @Override
    public void launchGame(int sessionId) {
        mSessionGameLauncherView.setProgressIndicator(true);

        mSessionService.start(sessionId, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                mSessionGameLauncherView.setProgressIndicator(false);
            }

            @Override
            public void failure(RetrofitError error) {
                mSessionGameLauncherView.setProgressIndicator(false);
                mSessionGameLauncherView.showErrorConnectionFailure(null);
            }
        });
    }

    @Override
    public void setView(@NonNull SessionGameLauncherContract.View view) {
        mSessionGameLauncherView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionGameLauncherView);
    }
}
