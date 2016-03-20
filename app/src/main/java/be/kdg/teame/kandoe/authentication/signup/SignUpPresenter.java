package be.kdg.teame.kandoe.authentication.signup;

import android.support.annotation.NonNull;

import java.util.Date;

import javax.inject.Inject;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.data.retrofit.services.SignUpService;
import be.kdg.teame.kandoe.models.users.dto.CreateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.exceptions.TokenException;
import be.kdg.teame.kandoe.util.http.ErrorResponse;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import be.kdg.teame.kandoe.util.validators.DTOValidator;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This class is responsible for managing and performing the actions that the user can initiate in {@link SignUpActivity}.
 * It implements {@link SignUpContract.UserActionsListener} and notifies the view after completing its actions.
 *
 * @see SignUpContract.UserActionsListener
 * */
public class SignUpPresenter implements SignUpContract.UserActionsListener {
    private SignUpContract.View mSingUpView;
    private final SignUpService mSignUpService;
    private final SignInService mSignInService;
    private final PrefManager prefManager;

    @Inject
    public SignUpPresenter(SignUpService mSignUpService, SignInService mSignInService, PrefManager prefManager) {
        this.mSignUpService = mSignUpService;
        this.mSignInService = mSignInService;
        this.prefManager = prefManager;
    }

    @Override
    public void setView(@NonNull SignUpContract.View view) {
        this.mSingUpView = view;
    }

    public void signIn() {
        mSingUpView.showSignIn();
    }

    @Override
    public void signUp(@NonNull final CreateUserDTO createUserDTO) {

        if (!DTOValidator.isValid(createUserDTO)) {
            mSingUpView.showErrorIncompleteDetails();
            return;
        }

        if (!createUserDTO.getPassword().equals(createUserDTO.getVerifyPassword())) {
            mSingUpView.showErrorNonMatchingPasswordFields();
            return;
        }

        mSingUpView.showProgressIndicator(true);

        mSignUpService.signUp(createUserDTO, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                signInUser(createUserDTO.getUsername(), createUserDTO.getPassword());
            }

            @Override
            public void failure(RetrofitError error) {
                mSingUpView.showProgressIndicator(false);

                if (error != null && error.getResponse() != null) {
                    if (error.getResponse().getStatus() == HttpStatus.UNPROCESSABLE_ENTITY) {

                        ErrorResponse errorResponse = (ErrorResponse) error.getBodyAs(ErrorResponse.class);
                        String firstError = errorResponse.getFieldErrors().get(0).getMessage();

                        mSingUpView.showErrorServerMessage(firstError);

                    } else if (error.getResponse().getStatus() == HttpStatus.BAD_REQUEST) {
                        ErrorResponse.FieldError fieldError = (ErrorResponse.FieldError) error.getBodyAs(ErrorResponse.FieldError.class);
                        mSingUpView.showErrorServerMessage(fieldError.getMessage());
                    } else {
                        mSingUpView.showErrorUserCreation();
                    }
                } else {
                    mSingUpView.showErrorConnectionFailure();
                }
            }
        });
    }

    /* *
     * Tries to login the user with a given username and password.
     * If successful, saves the {@link AccessToken} in the {@link PrefManager}.
     * Shows an error if not successful.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    private void signInUser(String username, String password) {
        mSignInService.signIn(SignInService.GRANT_TYPE_PASSWORD, username, password, new Callback<AccessToken>() {
            @Override
            public void success(AccessToken accessToken, Response response) {
                accessToken.setDateAcquired(new Date());

                try {
                    prefManager.saveAccessToken(accessToken);

                    mSingUpView.showProgressIndicator(false);
                    mSingUpView.showDashboard();
                } catch (TokenException e) {
                    mSingUpView.showProgressIndicator(false);
                    mSingUpView.showErrorInvalidToken();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mSingUpView.showProgressIndicator(false);
                mSingUpView.showErrorAutomaticSignInFailure();
            }
        });
    }
}
