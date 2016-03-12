package be.kdg.teame.kandoe.di.components;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.authentication.signup.SignUpActivity;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.dashboard.organizationlist.OrganizationListFragment;
import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListFragment;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.AuthenticationModule;
import be.kdg.teame.kandoe.di.modules.DashboardModule;
import be.kdg.teame.kandoe.di.modules.OrganizationModule;
import be.kdg.teame.kandoe.di.modules.SessionModule;
import be.kdg.teame.kandoe.di.modules.UserModule;
import be.kdg.teame.kandoe.profile.ProfileActivity;
import be.kdg.teame.kandoe.profile.edit.ProfileEditActivity;
import be.kdg.teame.kandoe.session.game.SessionGameActivity;
import dagger.Component;

/**
 * All classes which need dependency injection must be registered here.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                AuthenticationModule.class,
                UserModule.class,
                DashboardModule.class,
                OrganizationModule.class,
                SessionModule.class
        }

)
public interface AppComponent {
    void inject(App application);

    void inject(BaseActivity baseActivity);

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(DashboardActivity dashboardActivity);

    void inject(ProfileActivity profileActivity);

    void inject(ProfileEditActivity profileEditActivity);


    void inject(SessionGameActivity sessionGameActivity);

    void inject(BaseFragment baseFragment);

    void inject(OrganizationListFragment organizationListFragment);

    void inject(SessionListFragment sessionListFragment);
}
