package be.kdg.teame.kandoe.authentication;

import be.kdg.teame.kandoe.di.components.AppComponent;
import dagger.Component;

@Component(modules = { MockedAuthenticationModule.class })
public interface MockedAppComponent extends AppComponent {

}
