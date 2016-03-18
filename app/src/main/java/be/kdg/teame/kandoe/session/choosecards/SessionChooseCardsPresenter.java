package be.kdg.teame.kandoe.session.choosecards;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

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
        mChooseCardsView.setRefreshingProgressIndicator(true);
        mSessionService.getAllCards(sessionId, new Callback<List<CardDetails>>() {
            @Override
            public void success(List<CardDetails> cardDetails, Response response) {
                mChooseCardsView.setRefreshingProgressIndicator(false);
                mChooseCardsView.showCards(cardDetails);
            }

            @Override
            public void failure(RetrofitError error) {
                //todo show error
                mChooseCardsView.setRefreshingProgressIndicator(false);
            }
        });
    }

    @Override
    public void chooseCards(int sessionId, List<Integer> cardIds) {

        if (cardIds.isEmpty()) {
            mChooseCardsView.showNoCardsSelected();
            return;
        }

        mChooseCardsView.setProgressIndicator(true);
        mSessionService.chooseCards(sessionId, cardIds, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                mChooseCardsView.setProgressIndicator(false);
                Log.d(SessionChooseCardsPresenter.class.getSimpleName(), "Cards added successfully");
                mChooseCardsView.showWaitingForOtherParticipants();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(SessionChooseCardsPresenter.class.getSimpleName(), "Couldn't add cards: " + error.getMessage(), error);
                String errorMessage = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                try {
                    JSONObject jsonObject = new JSONObject(errorMessage);
                    mChooseCardsView.showErrorConnectionFailure(jsonObject.getString("message"));
                } catch (JSONException e) {
                    Log.d("Session-join", "JSONException: ".concat(e.getMessage()), e);
                    mChooseCardsView.showErrorConnectionFailure("Sorry, something went wrong.");
                }
            }
        });
    }
}
