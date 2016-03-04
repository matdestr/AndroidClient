package be.kdg.teame.kandoe.di.authentication;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.authentication.signin.SignInContract;
import be.kdg.teame.kandoe.authentication.signin.SignInPresenter;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class BaseMockSignInPresenter extends SignInPresenter {
    public SignInContract.View mSignInView;

    public BaseMockSignInPresenter(SignInService mSignInService, PrefManager prefManager) {
        super(mSignInService, prefManager);
    }


    @Override
    public void setView(@NonNull SignInContract.View view) {
        this.mSignInView = view;
    }
}
