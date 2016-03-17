package be.kdg.teame.kandoe.session.game.ranking;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.session.game.DataListener;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class SessionGameRankingPresenter implements SessionGameRankingContract.UserActionsListener {

    private final PrefManager mPrefManager;
    private SessionGameRankingContract.View mSessionGameRankingView;

    @Inject
    public SessionGameRankingPresenter(PrefManager prefManager) {
        this.mPrefManager = prefManager;
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
        Log.d(getClass().getSimpleName(), "Received cardpositions");
    }
}