package be.kdg.teame.kandoe.session.addcards;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SessionAddCardsPresenter implements SessionAddCardsContract.UserActionsListener{

    private SessionAddCardsContract.View mAddCardsView;

    private SessionService mSessionService;
    private PrefManager mPrefManager;

    @Inject
    public SessionAddCardsPresenter(SessionService mSessionService, PrefManager mPrefManager) {
        this.mPrefManager = mPrefManager;
        this.mSessionService = mSessionService;
    }

    @Override
    public void setView(@NonNull SessionAddCardsContract.View view) {
        mAddCardsView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mAddCardsView);
    }

    @Override
    public void loadCards(int sessionId) {
        mAddCardsView.setProgressIndicator(true);

       mSessionService.getAllCards(sessionId, new Callback<List<CardDetails>>() {
           @Override
           public void success(List<CardDetails> cardDetails, Response response) {
               mAddCardsView.setProgressIndicator(false);
               mAddCardsView.showCards(cardDetails);
           }

           @Override
           public void failure(RetrofitError error) {
               mAddCardsView.setProgressIndicator(false);
               String errorMessage = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
               try {
                   JSONObject jsonObject = new JSONObject(errorMessage);
                   mAddCardsView.showErrorConnectionFailure(jsonObject.getString("message"));
               } catch (JSONException e) {
                   Log.d("Session-join", "JSONException: ".concat(e.getMessage()), e);
                   mAddCardsView.showErrorConnectionFailure("Sorry, something went wrong.");
               }
           }
       });
    }

    @Override
    public void addCards(int sessionId) {
        mSessionService.addCards(sessionId, null, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
