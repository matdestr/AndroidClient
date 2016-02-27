package be.kdg.teame.kandoe.di.modules;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.authentication.signin.SignInContract;
import be.kdg.teame.kandoe.authentication.signin.SignInPresenter;
import be.kdg.teame.kandoe.data.retrofit.ServiceGenerator;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import dagger.Module;
import dagger.Provides;

@Module
public class AuthenticationModule {
    @Provides
    @Singleton
    public static SignInService provideSignInService() {
        return ServiceGenerator.createSignInService(SignInService.class);
    }

    /*
    // TODO : Serves as an example in case we forget ...
    @Provides
    @Singleton
    public static ProfileService provideProfileService(PrefManager prefManager) {
        return ServiceGenerator.createAuthorizedService(ProfileService.class, prefManager);
    }*/

    /*@Provides
    public SignInContract.UserActionsListener provideSignInPresenter(SignInContract.View signInView) {
        return new SignInPresenter(signInView, provideSignInService());
    }*/

    @Provides
    public SignInContract.UserActionsListener provideSignInPresenter(SignInService signInService, PrefManager prefManager) {
        return new SignInPresenter(signInService, prefManager);
    }
}
