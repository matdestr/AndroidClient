package be.kdg.teame.kandoe.authentication.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.authentication.signup.SignUpActivity;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.util.validators.forms.Form;
import be.kdg.teame.kandoe.util.validators.forms.FormField;
import butterknife.Bind;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity implements SignInContract.View {
    private ProgressDialog progressDialog;

    @Bind(R.id.form_username)
    EditText mEditTextUsername;

    @Bind(R.id.form_password)
    EditText mEditTextPassword;

    @Inject
    SignInContract.UserActionsListener mSignInPresenter;

    private Form mForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInPresenter.setView(this);
        progressDialog = DialogGenerator.createProgressDialog(this, R.string.sign_in_progress_message);

        TextInputLayout tilUsername = (TextInputLayout) mEditTextUsername.getParent();
        TextInputLayout tilPassword = (TextInputLayout) mEditTextPassword.getParent();

        mForm = new Form();

        mForm.add(new FormField(tilUsername, mEditTextUsername, FormField.Type.USER_NAME, true));
        mForm.add(new FormField(tilPassword, mEditTextPassword, FormField.Type.PASSWORD, true));

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
    public void showErrorInvalidToken() {
        DialogGenerator.showErrorDialog(this, R.string.sign_in_error_title, R.string.dialog_error_message_default);
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
        if (mForm.validate())
            mSignInPresenter.signIn(mEditTextUsername.getText().toString(), mEditTextPassword.getText().toString());

    }

    @OnClick(R.id.link_sign_up)
    protected void signUpClickHandler() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
