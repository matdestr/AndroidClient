package be.kdg.teame.kandoe.session.finish;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Presenter for the Session Finish feature
 */

public class SessionFinishPresenter implements SessionFinishContract.UserActionsListener {

    private SessionFinishContract.View mSessionFinishView;

    private final SessionService mSessionService;
    private final PrefManager mPrefManager;

    @Inject
    public SessionFinishPresenter(SessionService sessionService, PrefManager prefManager) {
        mSessionService = sessionService;
        mPrefManager = prefManager;
    }


    @Override
    public void setView(@NonNull SessionFinishContract.View view) {
        mSessionFinishView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionFinishView);
    }

    @Override
    public void getWinners(int sessionId) {
        mSessionService.getWinners(sessionId, new Callback<List<CardDetails>>() {
            @Override
            public void success(List<CardDetails> cardDetailses, Response response) {
                mSessionFinishView.setCards(cardDetailses);
            }

            @Override
            public void failure(RetrofitError error) {
                mSessionFinishView.showErrorConnectionFailure(error.getMessage());
            }
        });
    }
}
