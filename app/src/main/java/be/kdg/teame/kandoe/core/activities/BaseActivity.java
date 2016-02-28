package be.kdg.teame.kandoe.core.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.core.contracts.AuthenticatedView;
import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.di.components.AppComponent;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements AuthenticatedView {
    protected SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        startActivity(intent);
    }

    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    public SharedPreferences getSharedPreferences() {
        return preferences;
    }
}
