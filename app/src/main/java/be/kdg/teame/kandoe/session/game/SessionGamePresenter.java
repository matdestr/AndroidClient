package be.kdg.teame.kandoe.session.game;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.data.websockets.SocketService;
import be.kdg.teame.kandoe.data.websockets.WebSocketsManager;
import be.kdg.teame.kandoe.data.websockets.stomp.SubscriptionCallback;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.session.SessionActivity;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionGamePresenter implements SessionGameContract.UserActionsListener {

    private List<DataListener> mListeners;

    private SessionGameContract.View mSessionGameView;

    private SessionService mSessionService;
    private PrefManager mPrefManager;
    private SocketService mCurrentParticipantService;
    private SocketService mCardPositionService;



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
        if (listener != null) {
            this.mListeners.add(listener);
            Log.d(getClass().getSimpleName(), listener.getClass().getSimpleName().concat(" is ready to receive data."));
        }
    }

    private void notifyListeners(List<CardPosition> cardDetails) {
        Log.d(getClass().getSimpleName(), "Notifying " + mListeners.size() + " listeners about " + cardDetails.size() + " cards.");
        for (DataListener listener : mListeners)
            listener.onReceiveData(cardDetails);
    }

    @Override
    public void loadCardPositions(int sessionId) {
        mSessionService.getCardPositions(sessionId, new Callback<List<CardPosition>>() {
            @Override
            public void success(List<CardPosition> cardPositions, Response response) {
                Log.d(getClass().getSimpleName(), "Received " + cardPositions.size() + " positions.");
                notifyListeners(cardPositions);
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

    @Override
    public void openCurrentParticipantListener(int sessionId) {
        mCurrentParticipantService = new SocketService(
                "/topic/sessions/".concat(String.valueOf(sessionId)).concat("/current-participant"),
                "sessions_current_participant_subscription_id",
                new SubscriptionCallback() {
                    @Override
                    public void onMessage(Map<String, String> headers, String body) {
                        System.out.println(body);
                    }
                }
        );

        Thread thread = WebSocketsManager.getThread(mCurrentParticipantService, sessionId);

        if (!thread.isAlive())
            thread.start();
    }

    @Override
    public void closeCurrentParticipantListener() {
        if (mCurrentParticipantService != null)
            mCurrentParticipantService.stop();
    }

    @Override
    public void openCardPositionListener(final int sessionId) {
        mCardPositionService = new SocketService(
                "/topic/sessions/".concat(String.valueOf(sessionId)).concat("/positions"),
                "sessions_positions_subscription_id)",
                new SubscriptionCallback() {
                    @Override
                    public void onMessage(Map<String, String> headers, String body) {

                        SessionGamePresenter.this.loadCardPositions(sessionId);
                        Log.d(SessionGamePresenter.this.getClass().getSimpleName(), "Websocket sent update for cardpositions");
                    }
                });

        Thread thread = WebSocketsManager.getThread(mCardPositionService, sessionId);

        if (!thread.isAlive())
            thread.start();
    }

    @Override
    public void closeOpenCardPositionListener() {
        if (mCardPositionService != null)
            mCardPositionService.stop();
    }

}
