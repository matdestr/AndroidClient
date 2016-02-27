package be.kdg.teame.kandoe.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.util.preferences.PrefManager;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application mApp;

    public AppModule(Application application) {
        this.mApp = application;
    }

  // todo check if needed
    @Singleton
    @Provides
    public Context provideApplicationContext(){
        return mApp;
    }

    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }

    @Singleton
    @Provides
    public PrefManager providePrefManager(SharedPreferences sharedPreferences){
        return new PrefManager(sharedPreferences);
    }
}