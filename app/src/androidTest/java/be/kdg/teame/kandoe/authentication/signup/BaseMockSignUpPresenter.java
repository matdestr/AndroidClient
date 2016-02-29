package be.kdg.teame.kandoe.authentication.signup;

import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.data.retrofit.services.SignUpService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

class BaseMockSignUpPresenter extends SignUpPresenter {
    SignUpContract.View mSignUpView;

    public BaseMockSignUpPresenter(SignUpService mSignUpService, SignInService mSignInService, PrefManager prefManager) {
        super(mSignUpService, mSignInService, prefManager);
    }

    @Override
    public void setView(SignUpContract.View view) {
        this.mSignUpView = view;
    }
}
