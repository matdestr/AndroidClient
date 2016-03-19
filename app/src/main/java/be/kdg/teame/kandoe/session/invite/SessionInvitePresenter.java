package be.kdg.teame.kandoe.session.invite;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SessionInvitePresenter implements SessionInviteContract.UserActionsListener {
    private final PrefManager mPrefManager;
    private final SessionService mSessionService;

    @Inject
    SessionInviteContract.View mSessionInviteContractView;

    public SessionInvitePresenter(SessionService sessionService, PrefManager prefManager){
        mSessionService = sessionService;
        mPrefManager = prefManager;
    }

    @Override
    public void inviteUser(int sessionId, final String email) {
        mSessionService.invite(sessionId, email, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                mSessionInviteContractView.showUserInvited();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(SessionInvitePresenter.class.getSimpleName(), "Failed to invite user: ".concat(error.getMessage().concat(": ").concat("" + error.getKind())), error);

                if (error.getResponse().getBody() != null) {
                    String errorMessage = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    try {
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        mSessionInviteContractView.showInviteFailedError(jsonObject.getString("message"));

                    } catch (JSONException e) {
                        Log.d("Session-join", "JSONException: ".concat(e.getMessage()), e);
                        mSessionInviteContractView.showErrorConnectionFailure("Sorry, something went wrong.");
                    }
                } else {
                    mSessionInviteContractView.showInviteFailedError("Sorry, something went wrong.");
                }
            }
        });
    }

    @Override
    public void setView(@NonNull SessionInviteContract.View view) {
        this.mSessionInviteContractView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {

    }
}
