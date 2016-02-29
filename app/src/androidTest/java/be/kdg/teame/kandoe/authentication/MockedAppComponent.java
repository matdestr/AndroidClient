package be.kdg.teame.kandoe.authentication;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.authentication.signup.SignUpActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, MockedAuthenticationModule.class })
public interface MockedAppComponent extends AppComponent {
    void inject(SignUpActivity signUpActivity);
}
