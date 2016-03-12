package be.kdg.teame.kandoe.session.game;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class SessionGamePresenter implements SessionGameContract.UserActionsListener {

    private SessionGameContract.View mSessionGameView;

    private SessionService mSessionService;
    private PrefManager mPrefManager;

    @Inject
    public SessionGamePresenter(SessionService sessionService, PrefManager prefManager) {
        mSessionService = sessionService;
        mPrefManager = prefManager;
    }


    @Override
    public void setView(@NonNull SessionGameContract.View view) {
        mSessionGameView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionGameView);
    }
}
