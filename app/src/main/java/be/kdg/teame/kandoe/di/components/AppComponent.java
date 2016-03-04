package be.kdg.teame.kandoe.di.components;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.authentication.signup.SignUpActivity;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.dashboard.DashboardPresenter;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.AuthenticationModule;
import be.kdg.teame.kandoe.di.modules.DashboardModule;
import be.kdg.teame.kandoe.di.modules.UserModule;
import be.kdg.teame.kandoe.profile.ProfileActivity;
import be.kdg.teame.kandoe.profile.edit.ProfileEditActivity;
import dagger.Component;

/**
 * All classes which need dependency injection must be registered here.
 */
@Singleton
@Component(modules = {AppModule.class, AuthenticationModule.class, UserModule.class, DashboardModule.class})
public interface AppComponent {
    void inject(App application);

    void inject(BaseActivity baseActivity);

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(DashboardActivity dashboardActivity);

    void inject(ProfileActivity profileActivity);

    void inject(ProfileEditActivity profileEditActivity);

}
