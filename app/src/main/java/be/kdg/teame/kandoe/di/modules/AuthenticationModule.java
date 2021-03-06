package be.kdg.teame.kandoe.di.modules;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.authentication.signin.SignInContract;
import be.kdg.teame.kandoe.authentication.signin.SignInPresenter;
import be.kdg.teame.kandoe.authentication.signup.SignUpContract;
import be.kdg.teame.kandoe.authentication.signup.SignUpPresenter;
import be.kdg.teame.kandoe.data.retrofit.ServiceGenerator;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.data.retrofit.services.SignUpService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import dagger.Module;
import dagger.Provides;

/**
 * Module that provides objects for injection for authentication
 */

@Module
public class AuthenticationModule {
    @Provides
    @Singleton
    public SignInService provideSignInService() {
        return ServiceGenerator.createSignInService(SignInService.class);
    }

    @Provides
    @Singleton
    public SignUpService provideSignUpService() {
        return ServiceGenerator.createService(SignUpService.class);
    }

    @Provides
    public SignInContract.UserActionsListener provideSignInPresenter(SignInService signInService, PrefManager prefManager) {
        return new SignInPresenter(signInService, prefManager);
    }

    @Provides
    public SignUpContract.UserActionsListener provideSignUpPresenter(
            SignUpService signUpService, SignInService signInService, PrefManager prefManager) {
        return new SignUpPresenter(signUpService, signInService, prefManager);
    }
}
