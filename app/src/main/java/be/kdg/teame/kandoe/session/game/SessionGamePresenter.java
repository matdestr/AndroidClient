package be.kdg.teame.kandoe.session.game;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SessionGamePresenter implements SessionGameContract.UserActionsListener {

    private List<DataListener> mListeners;

    private SessionGameContract.View mSessionGameView;

    private SessionService mSessionService;
    private PrefManager mPrefManager;

    @Inject
    public SessionGamePresenter(SessionService sessionService, PrefManager prefManager) {
        mSessionService = sessionService;
        mPrefManager = prefManager;
        mListeners = new ArrayList<>();
    }


    @Override
    public void setView(@NonNull SessionGameContract.View view) {
        mSessionGameView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionGameView);
    }

    @Override
    public void addDataListener(DataListener listener) {
        //todo delete this log
        Log.wtf("wtf", "adding some listeners bruff");
        if (listener != null)
            this.mListeners.add(listener);
    }

    private void notifyListeners(){
        //Todo replace null with data
        for (DataListener listener : mListeners) {
            listener.onReceiveData(null);
        }
    }

    @Override
    public void loadCardPositions(int sessionId) {
        Log.d("loading card positions", "lol");
        notifyListeners();

        mSessionService.getCardPositions(sessionId, new Callback<List<CardPosition>>() {
            @Override
            public void success(List<CardPosition> cardPositions, Response response) {
                Log.d(getClass().getSimpleName(), "Received " + cardPositions.size() + " positions.");
                notifyListeners();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().getSimpleName(), "Failed to receive cardpositions: " + error.getMessage(), error);

                //todo fix this
                /*String errorMessage = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                try {
                    JSONObject jsonObject = new JSONObject(errorMessage);
                    mSessionGameView.showErrorConnectionFailure(jsonObject.getString("message"));
                } catch (JSONException e) {
                    Log.d("Session-join", "JSONException: ".concat(e.getMessage()), e);
                    mSessionGameView.showErrorConnectionFailure("Sorry, something went wrong.");
                }*/
            }
        });
    }

}
