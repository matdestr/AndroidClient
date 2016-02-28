package be.kdg.teame.kandoe.authentication;

import be.kdg.teame.kandoe.authentication.signin.SignInContract;
import be.kdg.teame.kandoe.authentication.signin.SignInPresenter;
import be.kdg.teame.kandoe.authentication.signup.SignUpContract;
import be.kdg.teame.kandoe.authentication.signup.SignUpPresenter;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.data.retrofit.services.SignUpService;
import be.kdg.teame.kandoe.di.modules.AuthenticationModule;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import dagger.Module;
import dagger.Provides;

@Module
public class MockedAuthenticationModule extends AuthenticationModule {
    private SignInPresenter signInPresenter;
    private SignUpPresenter signUpPresenter;

    private SignUpService signUpService;

    @Provides
    public SignInContract.UserActionsListener provideSignInPresenter(SignInService signInService, PrefManager prefManager) {
        if (signInPresenter == null)
            return new SignInPresenter(signInService, prefManager);

        return signInPresenter;
    }

    @Provides
    public SignUpContract.UserActionsListener provideSignUpPresenter(SignUpService signUpService, SignInService signInService, PrefManager prefManager) {
        if (signUpPresenter == null)
            return new SignUpPresenter(signUpService, signInService, prefManager);

        return signUpPresenter;
    }

    @Provides
    @Override
    public SignUpService provideSignUpService() {
        if (signUpService == null)
            return super.provideSignUpService();

        return signUpService;
    }

    public void setSignInPresenter(SignInPresenter signInPresenter) {
        this.signInPresenter = signInPresenter;
    }

    public void setSignUpPresenter(SignUpPresenter signUpPresenter) {
        this.signUpPresenter = signUpPresenter;
    }

    public void setSignUpService(SignUpService signUpService) {
        this.signUpService = signUpService;
    }
}
