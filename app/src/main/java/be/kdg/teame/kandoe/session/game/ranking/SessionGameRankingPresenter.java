package be.kdg.teame.kandoe.session.game.ranking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.models.error.ErrorMessage;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SessionGameRankingPresenter implements SessionGameRankingContract.UserActionsListener {

    private final PrefManager mPrefManager;
    private final SessionService mSessionService;
    private SessionGameRankingContract.View mSessionGameRankingView;

    @Inject
    public SessionGameRankingPresenter(SessionService sessionService, PrefManager prefManager) {
        this.mPrefManager = prefManager;
        this.mSessionService = sessionService;
    }

    @Override
    public void setView(@NonNull SessionGameRankingContract.View view) {
        mSessionGameRankingView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionGameRankingView);
    }

    @Override
    public void onReceiveData(List<CardPosition> cardPositions) {
        Log.d(getClass().getSimpleName(), "Received " + cardPositions.size() + " cardpositions");
        mSessionGameRankingView.showData(cardPositions);
    }

    @Override
    public void endGame(int sessionId) {
        mSessionService.end(sessionId, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                mSessionGameRankingView.setProgressIndicator(false);
                Log.d(getClass().getSimpleName(), String.format("[%d] %s", response.getStatus(), response.getBody()));
            }

            @Override
            public void failure(RetrofitError error) {
                mSessionGameRankingView.setProgressIndicator(false);
                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                Gson gson = new Gson();
                ErrorMessage errorMessage = gson.fromJson(json, ErrorMessage.class);

                mSessionGameRankingView.showErrorConnectionFailure(errorMessage.getMessage());
            }
        });
    }
}
