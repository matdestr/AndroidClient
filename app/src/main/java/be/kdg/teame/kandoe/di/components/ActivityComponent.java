package be.kdg.teame.kandoe.di.components;

import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.authentication.signup.SignUpActivity;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.profile.ProfileActivity;
import be.kdg.teame.kandoe.profile.edit.ProfileEditActivity;
import be.kdg.teame.kandoe.session.game.SessionGameFragment;

/*@ActivityScope
@Component(modules = {
        AuthenticationModule.class,
        UserModule.class,
        DashboardModule.class,
        SessionModule.class
})*/
public interface ActivityComponent {
    void inject(BaseActivity baseActivity);

    void inject(SignInActivity signInActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(DashboardActivity dashboardActivity);

    void inject(ProfileActivity profileActivity);

    void inject(ProfileEditActivity profileEditActivity);

    void inject(SessionGameFragment sessionGameFragment);
}
