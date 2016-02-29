package be.kdg.teame.kandoe.authentication.signin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.util.exceptions.TokenException;
import be.kdg.teame.kandoe.util.http.MockResponseFactory;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;

public class SignInPresenterTest {

    @Mock
    private SignInContract.View mSignInView;

    private SignInPresenter mSignInPresenter;

    @Mock
    private SignInService signInService;

    @Captor
    private ArgumentCaptor<Callback<AccessToken>> mAccessTokenCallbackCaptor;

    @Mock
    private PrefManager prefManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mSignInPresenter = new SignInPresenter(signInService, prefManager);
        mSignInPresenter.setView(mSignInView);
    }

    @Test
    public void successfulSignIn() throws TokenException {

        String username = "user";
        String password = "pass";

        mSignInPresenter.signIn(username, password);

        Mockito
                .verify(signInService)
                .signIn(Mockito.eq(SignInService.GRANT_TYPE_PASSWORD),
                        Mockito.eq(username), Mockito.eq(password),
                        mAccessTokenCallbackCaptor.capture());

        AccessToken accessToken = new AccessToken("dummy accessToken", "dummy tokenType", "dummy refreshToken", -1, new Date());

        mAccessTokenCallbackCaptor.getValue().success(accessToken, null);

        Mockito.verify(mSignInView).showProgressIndicator(Mockito.eq(true));

        Mockito.verify(prefManager).saveAccessToken(Mockito.eq(accessToken));

        Mockito.verify(mSignInView).showDashboard();
    }

    @Test
    public void signInWithWrongCredentials() {
        String username = "user";
        String password = "wrong-pass";

        mSignInPresenter.signIn(username, password);

        Mockito.verify(signInService)
                .signIn(Mockito.eq(SignInService.GRANT_TYPE_PASSWORD), Mockito.eq(username), Mockito.eq(password), mAccessTokenCallbackCaptor.capture());

        mAccessTokenCallbackCaptor.getValue().failure(
            RetrofitError.httpError(null, MockResponseFactory.getMockResponse(401), null, null)
        );

        Mockito.verify(mSignInView).showProgressIndicator(Mockito.eq(true));
        Mockito.verify(mSignInView, Mockito.never()).showDashboard();
        Mockito.verify(mSignInView, Mockito.never()).showSignUp();
        Mockito.verify(mSignInView).showErrorWrongCredentials();
    }

    @Test
    public void signInInternalServerError() {
        String username = "user";
        String password = "pass";

        mSignInPresenter.signIn(username, password);

        Mockito.verify(mSignInView).showProgressIndicator(Mockito.eq(true));
        Mockito.verify(signInService)
                .signIn(Mockito.eq(SignInService.GRANT_TYPE_PASSWORD),
                        Mockito.eq(username),
                        Mockito.eq(password),
                        mAccessTokenCallbackCaptor.capture());

        mAccessTokenCallbackCaptor.getValue().failure(null);
    }

    @Test
    public void signUp() {
        mSignInPresenter.signUp();
        Mockito.verify(mSignInView).showSignUp();
    }
}
