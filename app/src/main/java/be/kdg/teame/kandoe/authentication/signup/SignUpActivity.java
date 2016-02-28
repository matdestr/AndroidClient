package be.kdg.teame.kandoe.authentication.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.dto.CreateUserDTO;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements SignUpContract.View {
    @Bind(R.id.signup_username)
    EditText editTextUsername;

    @Bind(R.id.signup_first_name)
    EditText editTextFirstName;

    @Bind(R.id.signup_last_name)
    EditText editTextLastName;

    @Bind(R.id.signup_email)
    EditText editTextEmail;

    @Bind(R.id.signup_password)
    EditText editTextPassword;

    @Bind(R.id.signup_verifypassword)
    EditText editTextVerifyPassword;

    @Inject
    SignUpContract.UserActionsListener mSignUpPresenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mSignUpPresenter.setView(this);
        this.progressDialog = DialogGenerator.createProgressDialog(this, R.string.sign_up_progress_message);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @OnClick(R.id.btn_sign_up)
    protected void signUpClickHandler() {
        CreateUserDTO createUserDTO = new CreateUserDTO(
                editTextUsername.getText().toString(),
                editTextFirstName.getText().toString(),
                editTextLastName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString(),
                editTextVerifyPassword.getText().toString()
        );

        mSignUpPresenter.signUp(createUserDTO);
    }

    @OnClick(R.id.link_sign_in)
    protected void signInClickHandler() {
        mSignUpPresenter.signIn();
    }

    @Override
    public void showProgressIndicator(boolean active) {
        if (active)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }

    @Override
    public void showErrorConnectionFailure() {
        DialogGenerator.showErrorDialog(this, R.string.error_connection_failure);
    }

    @Override
    public void showSignIn() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void showErrorAutomaticSignInFailure() {

    }

    @Override
    public void showErrorIncompleteDetails() {

    }

    @Override
    public void showErrorNonMatchingPasswordFields() {

    }

    @Override
    public void showErrorUserCreation() {

    }

    @Override
    public void showErrorServerMessage(String reason) {

    }

    @Override
    public void showDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent, true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_signup;
    }
}
