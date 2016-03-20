package be.kdg.teame.kandoe.session.invite;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.users.dto.EmailDTO;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * This class is responsible for managing and performing the actions that the user can initiate in {@link SessionInviteFragment}.
 * It implements {@link SessionInviteContract.UserActionsListener} and notifies the view after completing its actions.
 *
 * @see SessionInviteContract.UserActionsListener
 * */
public class SessionInvitePresenter implements SessionInviteContract.UserActionsListener {
    private final PrefManager mPrefManager;
    private final SessionService mSessionService;

    private SessionInviteContract.View mSessionInviteContractView;

    public SessionInvitePresenter(SessionService sessionService, PrefManager prefManager) {
        mSessionService = sessionService;
        mPrefManager = prefManager;
    }

    @Override
    public void inviteUsers(final int sessionId, List<String> emails) {
        mSessionInviteContractView.setProgressIndicator(true);

        List<EmailDTO> emailDTOs = new ArrayList<>();
        for (String email : emails) {
            emailDTOs.add(new EmailDTO(email));
        }

        mSessionService.invite(sessionId, emailDTOs, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                mSessionService.confirmInvitation(sessionId, new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        mSessionInviteContractView.setProgressIndicator(false);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mSessionInviteContractView.setProgressIndicator(false);
                        mSessionInviteContractView.showErrorConnectionFailure(null);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                mSessionInviteContractView.setProgressIndicator(false);
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
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mSessionInviteContractView);
    }
}
