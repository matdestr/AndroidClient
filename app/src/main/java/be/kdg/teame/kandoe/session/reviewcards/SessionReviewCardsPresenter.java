package be.kdg.teame.kandoe.session.reviewcards;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.sessions.dto.CreateReviewDTO;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SessionReviewCardsPresenter implements SessionReviewCardsContract.UserActionsListener{

    private SessionReviewCardsContract.View mReviewCardsView;

    private SessionService mSessionService;
    private PrefManager mPrefManager;

    private Map<Integer, String> mCardDetailReviewsMap;

    @Inject
    public SessionReviewCardsPresenter(SessionService mSessionService, PrefManager mPrefManager) {
        this.mPrefManager = mPrefManager;
        this.mSessionService = mSessionService;
        this.mCardDetailReviewsMap = new HashMap<>();
    }

    @Override
    public void setView(@NonNull SessionReviewCardsContract.View view) {
        mReviewCardsView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mReviewCardsView);
    }

    @Override
    public void loadCards(int sessionId) {
        mReviewCardsView.setProgressIndicator(true);

        mSessionService.getAllCards(sessionId, new Callback<List<CardDetails>>() {
            @Override
            public void success(List<CardDetails> cardDetailsList, Response response) {
                mReviewCardsView.setProgressIndicator(false);
                mReviewCardsView.showCards(cardDetailsList);
            }

            @Override
            public void failure(RetrofitError error) {
                mReviewCardsView.setProgressIndicator(false);
                mReviewCardsView.showErrorConnectionFailure(null);
            }
        });
    }

    @Override
    public void saveReview(CardDetails cardDetails, String review) {
        if (review != null && review.length() > 0)
            this.mCardDetailReviewsMap.put(cardDetails.getCardDetailsId(), review);
    }

    @Override
    public void postReviews(final int sessionId) {
        if (this.mCardDetailReviewsMap.size() > 0) {
            mReviewCardsView.setProgressIndicator(true);

            List<CreateReviewDTO> reviewDTOs = new ArrayList<>();

            for (int cardDetailsId : mCardDetailReviewsMap.keySet()) {
                CreateReviewDTO r = new CreateReviewDTO();

                r.setCardDetailsId(cardDetailsId);
                r.setMessage(mCardDetailReviewsMap.get(cardDetailsId));

                reviewDTOs.add(r);
            }

            mSessionService.addReviews(sessionId, reviewDTOs, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    mReviewCardsView.setProgressIndicator(false);
                    mReviewCardsView.showWaitingForOtherParticipants();

                    confirmReviews(sessionId);
                }

                @Override
                public void failure(RetrofitError error) {
                    mReviewCardsView.setProgressIndicator(false);
                    String errorMessage = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    mReviewCardsView.showErrorConnectionFailure(convertJsonMessageToString(errorMessage));
                }
            });
        } else {
            confirmReviews(sessionId);
        }
    }

    private void confirmReviews(int sessionId) {
        mReviewCardsView.setProgressIndicator(true);

        mSessionService.confirmReviews(sessionId, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                mReviewCardsView.setProgressIndicator(false);
                mReviewCardsView.showWaitingForOtherParticipants();
            }

            @Override
            public void failure(RetrofitError error) {
                mReviewCardsView.setProgressIndicator(false);
                mReviewCardsView.showErrorConnectionFailure("Failed to confirm reviews");
            }
        });
    }

    private String convertJsonMessageToString(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("message");
        } catch (JSONException e) {
            Log.d("Session-review", "JSONException: ".concat(e.getMessage()), e);
            return null;
        }
    }
}
