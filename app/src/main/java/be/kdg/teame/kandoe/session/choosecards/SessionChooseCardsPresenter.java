package be.kdg.teame.kandoe.session.choosecards;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class SessionChooseCardsPresenter implements SessionChooseCardsContract.UserActionsListener {

    private SessionChooseCardsContract.View mChooseCardsView;

    private SessionService mSessionService;
    private PrefManager mPrefManager;

    @Inject
    public SessionChooseCardsPresenter(SessionService mSessionService, PrefManager mPrefManager) {
        this.mPrefManager = mPrefManager;
        this.mSessionService = mSessionService;
    }

    @Override
    public void setView(@NonNull SessionChooseCardsContract.View view) {

    }

    @Override
    public void checkUserIsAuthenticated() {

    }

    @Override
    public void loadCards(int sessionId) {

    }
}
