package be.kdg.teame.kandoe.authentication.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.authentication.signup.SignUpActivity;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity implements SignInContract.View {
    private ProgressDialog progressDialog;

    @Bind(R.id.signin_username)
    EditText mUsername;

    @Bind(R.id.signin_password)
    EditText mPassword;

    @Inject
    SignInContract.UserActionsListener mSignInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInPresenter.setView(this);
        progressDialog = DialogGenerator.createProgressDialog(this, R.string.sign_in_progress_message);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_signin;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showProgressIndicator(boolean active) {
        if (active)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }

    @Override
    public void showErrorWrongCredentials() {
        DialogGenerator.showErrorDialog(this, R.string.sign_in_error_title, R.string.sign_in_error_wrong_credentials);
    }

    @Override
    public void showErrorConnectionFailure() {
        DialogGenerator.showErrorDialog(this, R.string.error_connection_failure);
    }

    @Override
    public void showDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent, true);
    }

    @Override
    public void showSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_sign_in)
    protected void signInClickHandler() {
        mSignInPresenter.signIn(mUsername.getText().toString(), mPassword.getText().toString());

    }

    @OnClick(R.id.link_sign_up)
    protected void signUpClickHandler() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
