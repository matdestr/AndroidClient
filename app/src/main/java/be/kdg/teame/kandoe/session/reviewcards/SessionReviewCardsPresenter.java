package be.kdg.teame.kandoe.session.reviewcards;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class SessionReviewCardsPresenter implements SessionReviewCardsContract.UserActionsListener{

    private SessionReviewCardsContract.View mAddCardsView;

    private SessionService mSessionService;
    private PrefManager mPrefManager;

    @Inject
    public SessionReviewCardsPresenter(SessionService mSessionService, PrefManager mPrefManager) {
        this.mPrefManager = mPrefManager;
        this.mSessionService = mSessionService;
    }

    @Override
    public void setView(@NonNull SessionReviewCardsContract.View view) {
        mAddCardsView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
    AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mAddCardsView);
    }
}
