package be.kdg.teame.kandoe.authentication.signup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.data.retrofit.services.SignUpService;
import be.kdg.teame.kandoe.models.dto.CreateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.http.MockResponseFactory;
import be.kdg.teame.kandoe.util.http.MockTokenFactory;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

public class SignUpPresenterTest {
    @Mock
    private SignUpContract.View mSignUpView;

    private SignUpPresenter mSignUpPresenter;

    @Mock
    private SignUpService mSignUpService;

    @Mock
    private SignInService mSignInService;

    @Mock
    private PrefManager prefManager;

    @Captor
    private ArgumentCaptor<Callback<User>> mUserCallbackCaptor;

    @Captor
    private ArgumentCaptor<Callback<AccessToken>> mAccessTokenCallbackCaptor;

    /* Default user credentials */
    private String username = "newuser";
    private String firstName = "FirstName";
    private String lastName = "LastName";
    private String email = "newuser@cando.com";
    private String password = "pass";
    private String verifyPassword = "pass";

    private CreateUserDTO createUserDTODefault;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mSignUpPresenter = new SignUpPresenter(mSignUpService, mSignInService, prefManager);
        mSignUpPresenter.setView(mSignUpView);

        createUserDTODefault = new CreateUserDTO(
                username,
                firstName,
                lastName,
                email,
                password,
                verifyPassword
        );
    }

    @Test
    public void successfulSignUp() {
        AccessToken accessToken = MockTokenFactory.getMockAccessToken();

        mSignUpPresenter.signUp(createUserDTODefault);

        Mockito.verify(mSignUpService)
                .signUp(Mockito.eq(createUserDTODefault), mUserCallbackCaptor.capture());

        User user = new User(1, username, firstName, lastName, email);

        mUserCallbackCaptor.getValue().success(user, null);

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(true));

        Mockito.verify(mSignInService)
                .signIn(Mockito.eq(SignInService.GRANT_TYPE_PASSWORD),
                        Mockito.eq(username), Mockito.eq(password),
                        mAccessTokenCallbackCaptor.capture());

        mAccessTokenCallbackCaptor.getValue().success(accessToken, null);

        Mockito.verify(prefManager).saveAccessToken(Mockito.any(AccessToken.class));
        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(false));
        Mockito.verify(mSignUpView).showDashboard();
    }

    @Test
    public void signUpWithIncompleteUserDetails() {
        CreateUserDTO createUserDTO = new CreateUserDTO("newuser", "New", "User", "", "", "");
        mSignUpPresenter.signUp(createUserDTO);
        Mockito.verify(mSignUpView).showErrorIncompleteDetails();
    }

    @Test
    public void signUpWithNonMatchingPasswordFields() {
        CreateUserDTO createUserDTO = new CreateUserDTO(
                "newuser", "FirstName", "LastName", "newuser@cando.com", "pass", "differentpass"
        );

        mSignUpPresenter.signUp(createUserDTO);
        Mockito.verify(mSignUpView).showErrorNonMatchingPasswordFields();
    }

    @Test
    public void failedSignUp() {
        mSignUpPresenter.signUp(createUserDTODefault);

        Mockito.verify(mSignUpService)
                .signUp(Mockito.eq(createUserDTODefault), mUserCallbackCaptor.capture());

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(true));

        mUserCallbackCaptor.getValue().failure(null);

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(false));
        Mockito.verify(mSignUpView).showErrorConnectionFailure();
    }

    @Test
    public void successfulSignUpAndFailedAutomaticSignIn() {
        mSignUpPresenter.signUp(createUserDTODefault);

        Mockito.verify(mSignUpService)
                .signUp(Mockito.eq(createUserDTODefault), mUserCallbackCaptor.capture());

        User user = new User(1, username, firstName, lastName, email);

        mUserCallbackCaptor.getValue().success(user, null);

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(true));

        Mockito.verify(mSignInService)
                .signIn(Mockito.eq(SignInService.GRANT_TYPE_PASSWORD),
                        Mockito.eq(username), Mockito.eq(password),
                        mAccessTokenCallbackCaptor.capture());

        mAccessTokenCallbackCaptor.getValue().failure(
                RetrofitError.httpError("", MockResponseFactory.getMockResponse(HttpStatus.INTERNAL_SERVER_ERROR), null, null)
        );

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(false));
        Mockito.verify(mSignUpView).showErrorAutomaticSignInFailure();
    }

    @Test
    public void signUpWithExistingUsername() throws JSONException {
        String jsonErrorResponse = "{\"fieldErrors\":[" +
                "{\"message\":\"Username already in use\", \"field\":\"username\"}" +
                "]}";

        String gson = new Gson().toJson(jsonErrorResponse, String.class);

        mSignUpPresenter.signUp(createUserDTODefault);

        Mockito.verify(mSignUpService)
                .signUp(Mockito.eq(createUserDTODefault), mUserCallbackCaptor.capture());

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(true));

        mUserCallbackCaptor.getValue().failure(
                RetrofitError.httpError(null,
                        MockResponseFactory.getMockResponse(HttpStatus.UNPROCESSABLE_ENTITY, "", new TypedString(gson)),
                        new GsonConverter(new GsonBuilder().create()), null)
        );

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(false));
        Mockito.verify(mSignUpView).showErrorServerMessage(Mockito.matches("Username already in use"));
    }

    @Test
    public void signUpInternalServerError() {
        mSignUpPresenter.signUp(createUserDTODefault);

        Mockito.verify(mSignUpService)
                .signUp(Mockito.eq(createUserDTODefault), mUserCallbackCaptor.capture());

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(true));

        mUserCallbackCaptor.getValue().failure(RetrofitError.httpError(
                null, MockResponseFactory.getMockResponse(500), null, null
        ));

        Mockito.verify(mSignUpView).showProgressIndicator(Mockito.eq(false));
        Mockito.verify(mSignUpView).showErrorUserCreation();
    }

    @Test
    public void switchToSignIn() {
        mSignUpPresenter.signIn();
        Mockito.verify(mSignUpView).showSignIn();
    }
}
