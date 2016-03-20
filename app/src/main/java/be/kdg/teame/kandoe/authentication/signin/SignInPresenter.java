package be.kdg.teame.kandoe.authentication.signin;

import android.support.annotation.NonNull;

import java.util.Date;

import javax.inject.Inject;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.util.exceptions.TokenException;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This class is responsible for managing and performing the actions that the user can initiate in {@link SignInActivity}.
 * It implements {@link SignInContract.UserActionsListener} and notifies the view after completing its actions.
 *
 * @see SignInContract.UserActionsListener
 * */
public class SignInPresenter implements SignInContract.UserActionsListener {
    private SignInContract.View mSignInView;
    private final SignInService signInService;
    private final PrefManager mPrefManager;

    @Inject
    public SignInPresenter(SignInService signInService, PrefManager mPrefManager) {
        this.signInService = signInService;
        this.mPrefManager = mPrefManager;
    }

    @Override
    public void setView(@NonNull SignInContract.View view) {
        this.mSignInView = view;
    }

    @Override
    public void signIn(final String username, String password) {
        mSignInView.showProgressIndicator(true);

        this.signInService.signIn(SignInService.GRANT_TYPE_PASSWORD, username, password, new Callback<AccessToken>() {
            @Override
            public void success(AccessToken accessToken, Response response) {
                accessToken.setDateAcquired(new Date());

                try {
                    mPrefManager.saveAccessToken(accessToken);
                    mPrefManager.saveUsername(username);

                    mSignInView.showProgressIndicator(false);
                    mSignInView.showDashboard();
                } catch (TokenException e) {
                    mSignInView.showErrorInvalidToken();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                mSignInView.showProgressIndicator(false);

                if (error != null && error.getResponse() != null) {
                    if (error.getResponse().getStatus() == 400 || error.getResponse().getStatus() == 401) {
                        mSignInView.showErrorWrongCredentials();

                    } else {
                        mSignInView.showErrorConnectionFailure("Unable to retrieve profile information for " + mPrefManager.retrieveUsername());
                    }
                } else {
                    mSignInView.showErrorConnectionFailure(null);
                }
            }
        });
    }

    @Override
    public void signUp() {
        mSignInView.showSignUp();
    }

    /*@Override
    public String getSignInErrorMessage(int statusCode) {
        switch (statusCode){

        }
    }*/
}
