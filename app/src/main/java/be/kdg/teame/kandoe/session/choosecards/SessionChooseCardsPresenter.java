package be.kdg.teame.kandoe.session.choosecards;

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
        this.mChooseCardsView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mChooseCardsView);
    }

    @Override
    public void loadCards(int sessionId) {
        mSessionService.getAllCards(sessionId, new Callback<List<CardDetails>>() {
            @Override
            public void success(List<CardDetails> cardDetails, Response response) {
                mChooseCardsView.showCards(cardDetails);
            }

            @Override
            public void failure(RetrofitError error) {
                //todo show error
            }
        });
    }
}
