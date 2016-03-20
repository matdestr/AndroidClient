package be.kdg.teame.kandoe;

import android.app.Application;

import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.di.components.DaggerAppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;

/**
 * Contains the global state of the application.
 */
public final class App extends Application {
    private AppComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent component() {
        return mComponent;
    }

    public void setComponent(AppComponent appComponent) {
        this.mComponent = appComponent;
    }
}
