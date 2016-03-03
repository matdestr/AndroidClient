package be.kdg.teame.kandoe.di;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.authentication.signup.SignUpContract;
import be.kdg.teame.kandoe.authentication.signup.SignUpPresenter;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.data.retrofit.services.SignUpService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class BaseMockSignUpPresenter extends SignUpPresenter {
    public SignUpContract.View mSignUpView;

    public BaseMockSignUpPresenter(SignUpService mSignUpService, SignInService mSignInService, PrefManager prefManager) {
        super(mSignUpService, mSignInService, prefManager);
    }

    @Override
    public void setView(@NonNull SignUpContract.View view) {
        this.mSignUpView = view;
    }
}
