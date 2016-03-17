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

    private List<CardDetails> mAllCards;
    private List<Integer> mSelectedCards;

    @Inject
    public SessionChooseCardsPresenter(SessionService mSessionService, PrefManager mPrefManager) {
        this.mPrefManager = mPrefManager;
        this.mSessionService = mSessionService;
        this.mSelectedCards = new ArrayList<>();
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
        mChooseCardsView.setProgressIndicator(true);
        mSessionService.getAllCards(sessionId, new Callback<List<CardDetails>>() {
            @Override
            public void success(List<CardDetails> cardDetails, Response response) {
                mChooseCardsView.setProgressIndicator(false);
                mAllCards = cardDetails;
                mChooseCardsView.showCards(cardDetails);
            }

            @Override
            public void failure(RetrofitError error) {
                //todo show error
                mChooseCardsView.setProgressIndicator(false);
            }
        });
    }

    @Override
    public void chooseCard(int index) {
        if (mAllCards != null)
            if (mSelectedCards.contains(mAllCards.get(index).getCardDetailsId())) {
                mSelectedCards.remove(index);
                Log.d(SessionChooseCardsPresenter.class.getSimpleName(), "Removed card " + mAllCards.get(index).getText());
            }
            else {
                this.mSelectedCards.add(mAllCards.get(index).getCardDetailsId());
                Log.d(SessionChooseCardsPresenter.class.getSimpleName(), "Added card " + mAllCards.get(index).getText());
            }
    }

    @Override
    public void chooseCards(int sessionId) {
        if (mSelectedCards.isEmpty()) return;

        Log.d(SessionChooseCardsPresenter.class.getSimpleName(), "Adding " + mSelectedCards.size() + " cards");

        mSessionService.chooseCards(sessionId, mSelectedCards, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d(SessionChooseCardsPresenter.class.getSimpleName(), "Cards added successfully");
                mChooseCardsView.onChooseCardsCompleted();
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
