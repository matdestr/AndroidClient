package be.kdg.teame.kandoe.core.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements AuthenticatedContract.View {
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
    public void launchUnauthenticatedRedirectActivity() {
        Intent intent = new Intent(BaseActivity.this, Injector.getUnauthenticatedRedirectActivity());
        startActivity(intent, true);
    }

    @Override
    public void launchUnauthenticatedRedirectActivity(String reason) {
        DialogGenerator.showErrorDialog(this, R.string.dialog_error_title_unauthenticated, R.string.dialog_error_message_unauthenticated, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                launchUnauthenticatedRedirectActivity();
            }
        });
    }

    public void startActivity(Intent intent, boolean clearStack) {
        if (clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
