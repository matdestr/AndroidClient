package be.kdg.teame.kandoe.di.components;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.AuthenticationModule;
import dagger.Component;

/**
 * All classes which need dependency injection must be registered here.
 */
@Singleton
@Component(modules = {AppModule.class, AuthenticationModule.class})
public interface AppComponent {
    void inject(App application);

    void inject(BaseActivity baseActivity);

    void inject(SignInActivity signInActivity);
}
