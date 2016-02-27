package be.kdg.teame.kandoe.authentication.signin;

import javax.inject.Inject;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignInPresenter implements SignInContract.UserActionsListener {
    private SignInContract.View mSignInView;
    private final SignInService signInService;
    private final PrefManager prefManager;
    /*@Inject
    public SignInPresenter(SignInContract.View mSignInView, SignInService signInService) {
        this.mSignInView = mSignInView;
        this.signInService = signInService;
    }*/

    @Inject
    public SignInPresenter(SignInService signInService, PrefManager prefManager) {
        this.signInService = signInService;
        this.prefManager = prefManager;
    }

    @Override
    public void setView(SignInContract.View view) {
        this.mSignInView = view;
    }

    public void setSignInView(SignInContract.View mSignInView) {
        this.mSignInView = mSignInView;
    }

    @Override
    public void signIn(String username, String password) {
        mSignInView.showProgressIndicator(true);

        this.signInService.signIn(SignInService.GRANT_TYPE_PASSWORD, username, password, new Callback<AccessToken>() {
            @Override
            public void success(AccessToken accessToken, Response response) {
                prefManager.saveAccessToken(accessToken);

                mSignInView.showProgressIndicator(false);
                mSignInView.showDashboard();
            }

            @Override
            public void failure(RetrofitError error) {
                mSignInView.showProgressIndicator(false);

                if (error != null && error.getResponse() != null) {
                    if (error.getResponse().getStatus() == 400 || error.getResponse().getStatus() == 401) {
                        mSignInView.showErrorWrongCredentials();

                    } else {
                        mSignInView.showErrorConnectionFailure();
                    }
                } else {
                    mSignInView.showErrorConnectionFailure();
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
