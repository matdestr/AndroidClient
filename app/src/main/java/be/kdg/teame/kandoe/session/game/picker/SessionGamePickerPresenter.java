package be.kdg.teame.kandoe.session.game.picker;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class SessionGamePickerPresenter implements SessionGamePickerContract.UserActionsListener {

    private final PrefManager mPrefManager;
    private SessionGamePickerContract.View mSessionGamePickerView;
    private List<CardDetails> cards;

    @Inject
    public SessionGamePickerPresenter(PrefManager prefManager) {
        mPrefManager = prefManager;
        cards = new ArrayList<>();
    }


    @Override
    public void loadNextCard() {

    }

    @Override
    public void loadPreviousCard() {

    }

    @Override
    public void pickCard() {

    }

    @Override
    public void setView(@NonNull SessionGamePickerContract.View view) {
        mSessionGamePickerView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionGamePickerView);
    }
}
