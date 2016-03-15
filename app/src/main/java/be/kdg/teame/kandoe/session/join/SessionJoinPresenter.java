package be.kdg.teame.kandoe.session.join;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.data.websockets.StompService;
import be.kdg.teame.kandoe.data.websockets.WebSocketsConnector;
import be.kdg.teame.kandoe.data.websockets.stomp.Stomp;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SessionJoinPresenter implements SessionJoinContract.UserActionsListener, StompService {
    private final SessionService mSessionService;
    private final PrefManager mPrefManager;

    private SessionJoinContract.View mSessionJoinView;

    @Inject
    public SessionJoinPresenter(SessionService sessionService, PrefManager prefManager) {
        this.mSessionService = sessionService;
        this.mPrefManager = prefManager;
    }

    @Override
    public void join(int sessionId) {
        mSessionJoinView.setProgressIndicator(true);


       new WebSocketsConnector(this, mPrefManager).execute();

    /*
     //todo implement join-service
     mSessionService.join(sessionId, new Callback() {
            @Override
            public void success(Object o, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                mSessionJoinView.setProgressIndicator(false);
                mSessionJoinView.showErrorConnectionFailure(null);
            }
        });*/
    }

    @Override
    public void setView(@NonNull SessionJoinContract.View view) {
        mSessionJoinView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionJoinView);
    }

    @Override
    public void onConnected(Stomp client) {
        client.connect();
    }
}
