package be.kdg.teame.kandoe.session.game.picker;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.models.error.ErrorMessage;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.session.game.DataListener;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SessionGamePickerPresenter implements SessionGamePickerContract.UserActionsListener {

    private final SessionService mSessionService;
    private final PrefManager mPrefManager;
    private SessionGamePickerContract.View mSessionGamePickerView;
    private List<CardPosition> mCardPositions;
    private int mCurrentCardIndex;
    private CardPosition mCurrentPosition;

    @Inject
    public SessionGamePickerPresenter(SessionService sessionService, PrefManager prefManager) {
        mSessionService = sessionService;
        mPrefManager = prefManager;
        mCurrentCardIndex = 0;
    }


    @Override
    public void loadNextCard() {
        mCurrentCardIndex++;
        if (mCurrentCardIndex >= mCardPositions.size())
            mCurrentCardIndex = 0;

        mCurrentPosition = mCardPositions.get(mCurrentCardIndex);
        mSessionGamePickerView.showNextCard(mCurrentPosition.getCardDetails());
    }

    @Override
    public void loadPreviousCard() {
        mCurrentCardIndex--;
        if (mCurrentCardIndex <= 0)
            mCurrentCardIndex = mCardPositions.size() - 1;

        mCurrentPosition = mCardPositions.get(mCurrentCardIndex);
        mSessionGamePickerView.showPreviousCard(mCurrentPosition.getCardDetails());
    }

    @Override
    public void pickCard(int sessionId) {
        mSessionGamePickerView.setProgressIndicator(true);

        mSessionService.increaseCardPriority(sessionId, mCurrentPosition.getCardDetails().getCardDetailsId(), new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                mSessionGamePickerView.setProgressIndicator(false);
                Log.d(getClass().getSimpleName(), String.format("[%d] %s", response.getStatus(), response.getBody()));

            }

            @Override
            public void failure(RetrofitError error) {
                mSessionGamePickerView.setProgressIndicator(false);
                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                Gson gson = new Gson();
                ErrorMessage errorMessage = gson.fromJson(json, ErrorMessage.class);

                mSessionGamePickerView.showErrorConnectionFailure(errorMessage.getMessage());
            }
        });
    }

    @Override
    public void setView(@NonNull SessionGamePickerContract.View view) {
        mSessionGamePickerView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionGamePickerView);
    }

    @Override
    public void onReceiveData(List<CardPosition> cardPositions) {
        Log.d(getClass().getSimpleName(), "Received " + cardPositions.size() + " cardpositions");
        mCardPositions = cardPositions;
        mCurrentPosition = cardPositions.get(0);
        mSessionGamePickerView.showCard(mCurrentPosition.getCardDetails());
        //mSessionGamePickerView.updateData(cardPositions);
    }
}
