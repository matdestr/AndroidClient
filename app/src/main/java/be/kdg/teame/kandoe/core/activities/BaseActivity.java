package be.kdg.teame.kandoe.core.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.core.contracts.AuthenticatedView;
import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements AuthenticatedView {
    @Inject
    protected PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResource());
        ButterKnife.bind(this);

        injectComponent(((App) getApplication()).component());
    }

    protected abstract int getLayoutResource();

    @Override
    public void launchUnauthorizedRedirectActivity() {
        Intent intent = new Intent(this, Injector.getUnauthenticatedRedirectActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startActivity(Intent intent, boolean clearStack) {
        if (clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        startActivity(intent);
    }

    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }
}
