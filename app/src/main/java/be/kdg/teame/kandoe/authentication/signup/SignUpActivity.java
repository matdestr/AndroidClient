package be.kdg.teame.kandoe.authentication.signup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.dto.CreateUserDTO;
import be.kdg.teame.kandoe.util.validators.forms.Form;
import be.kdg.teame.kandoe.util.validators.forms.FormField;
import butterknife.Bind;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements SignUpContract.View {
    @Bind(R.id.form_username)
    EditText mEditTextUsername;

    @Bind(R.id.form_first_name)
    EditText mEditTextFirstName;

    @Bind(R.id.form_last_name)
    EditText mEditTextLastName;

    @Bind(R.id.form_email)
    EditText mEditTextEmail;

    @Bind(R.id.form_password)
    EditText mEditTextPassword;

    @Bind(R.id.form_verifypassword)
    EditText mEditTextVerifyPassword;


    @Inject
    SignUpContract.UserActionsListener mSignUpPresenter;

    private ProgressDialog mProgressDialog;
    private Form mFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mSignUpPresenter.setView(this);
        this.mProgressDialog = DialogGenerator.createProgressDialog(this, R.string.sign_up_progress_message);

        mFrom = new Form();

        TextInputLayout tilUsername = (TextInputLayout) mEditTextUsername.getParent();
        TextInputLayout tilFirstName = (TextInputLayout) mEditTextFirstName.getParent();
        TextInputLayout tilLastName = (TextInputLayout) mEditTextLastName.getParent();
        TextInputLayout tilEmail = (TextInputLayout) mEditTextEmail.getParent();
        TextInputLayout tilPassword = (TextInputLayout) mEditTextPassword.getParent();
        TextInputLayout tilVerifyPassword = (TextInputLayout) mEditTextVerifyPassword.getParent();

        mFrom.add(new FormField(tilUsername, mEditTextUsername, FormField.Type.USER_NAME, true));
        mFrom.add(new FormField(tilFirstName, mEditTextFirstName, true));
        mFrom.add(new FormField(tilLastName, mEditTextLastName, true));
        mFrom.add(new FormField(tilEmail, mEditTextEmail, FormField.Type.EMAIL, true));
        mFrom.add(new FormField(tilPassword, mEditTextPassword, FormField.Type.PASSWORD, true));
        mFrom.add(new FormField(tilVerifyPassword, mEditTextVerifyPassword, FormField.Type.VERIFY, true, mEditTextPassword));
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @OnClick(R.id.btn_sign_up)
    protected void signUpClickHandler() {

        if (mFrom.validate()) {
            CreateUserDTO createUserDTO = new CreateUserDTO(
                    mEditTextUsername.getText().toString(),
                    mEditTextFirstName.getText().toString(),
                    mEditTextLastName.getText().toString(),
                    mEditTextEmail.getText().toString(),
                    mEditTextPassword.getText().toString(),
                    mEditTextVerifyPassword.getText().toString()
            );

            mSignUpPresenter.signUp(createUserDTO);
        }
    }

    @OnClick(R.id.link_sign_in)
    protected void signInClickHandler() {
        mSignUpPresenter.signIn();
    }

    @Override
    public void showProgressIndicator(boolean active) {
        if (active)
            mProgressDialog.show();
        else
            mProgressDialog.dismiss();
    }

    @Override
    public void showErrorConnectionFailure() {
        DialogGenerator.showErrorDialog(this, R.string.error_connection_failure);
    }

    @Override
    public void showSignIn() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent, true);
    }

    @Override
    public void showErrorAutomaticSignInFailure() {
        DialogGenerator.showErrorDialog(this, R.string.dialog_error_message_default);
    }

    @Override
    public void showErrorIncompleteDetails() {
        DialogGenerator.showErrorDialog(this, R.string.dialog_error_message_default);
    }

    @Override
    public void showErrorNonMatchingPasswordFields() {
        DialogGenerator.showErrorDialog(this, R.string.dialog_error_message_default);
    }

    @Override
    public void showErrorUserCreation() {
        DialogGenerator.showErrorDialog(this, R.string.dialog_error_user_creation);
    }

    @Override
    public void showErrorServerMessage(String reason) {
        DialogGenerator.showErrorDialog(this, reason);
    }

    @Override
    public void showErrorInvalidToken() {
        DialogGenerator.showErrorDialog(this, R.string.sign_in_error_title, R.string.dialog_error_message_default, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showSignIn();
            }
        });
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
