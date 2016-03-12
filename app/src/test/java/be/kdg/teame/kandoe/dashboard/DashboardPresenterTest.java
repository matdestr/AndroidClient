package be.kdg.teame.kandoe.dashboard;

import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.http.ErrorResponse;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.http.MockResponseFactory;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

@RunWith(MockitoJUnitRunner.class)
public class DashboardPresenterTest {

    @Mock
    private DashboardContract.View mDashboardView;

    private DashboardContract.UserActionsListener mDashboardPresenter;

    @Mock
    private UserService mUserService;

    @Mock
    private PrefManager mPrefManager;

    @Captor
    private ArgumentCaptor<Callback<User>> mUserCallbackCaptor;


    @Before
    public void setUp() {
        mDashboardPresenter = new DashboardPresenter(mUserService, mPrefManager);
        mDashboardPresenter.setView(mDashboardView);
    }

    @Test
    public void authenticatedAccess(){
        AccessToken validAccessToken = new AccessToken("access-token", "token-type", "refresh-token", 1, new Date());

        Mockito.when(mPrefManager.retrieveAccessToken()).thenReturn(validAccessToken);

        mDashboardPresenter.checkUserIsAuthenticated();

        Mockito.verifyZeroInteractions(mDashboardView);
    }

    @Test
    public void unauthenticatedAccess(){
        AccessToken invalidAccessToken = new AccessToken(null, null, null, -1, null);

        Mockito.when(mPrefManager.retrieveAccessToken()).thenReturn(invalidAccessToken);

        mDashboardPresenter.checkUserIsAuthenticated();

        Mockito.verify(mDashboardView).launchUnauthenticatedRedirectActivity();
    }

    @Test
    public void authenticatedRetrieveUserdata(){
        Mockito.when(mPrefManager.retrieveUsername()).thenReturn("username");

        mDashboardPresenter.loadUserdata();

        Mockito.verify(mUserService)
                .getUser(Mockito.eq("username"), mUserCallbackCaptor.capture());

        User user = new User(0, "user", "firstname", "lastname", "user@cando.com", null);

        mUserCallbackCaptor.getValue().success(user, null);

        Mockito.verify(mDashboardView).showUserdata(Mockito.eq(user));
    }

    @Test
    public void unauthenticatedRetrieveUserdata() throws JSONException {
        Mockito.when(mPrefManager.retrieveUsername()).thenReturn("username");

        mDashboardPresenter.loadUserdata();

        Mockito.verify(mUserService)
                .getUser(Mockito.eq("username"), mUserCallbackCaptor.capture());


        RetrofitError retrofitError =
                RetrofitError.httpError(null,
                        MockResponseFactory.getMockResponse(HttpStatus.UNAUTHORIZED, "", null),
                        new GsonConverter(new GsonBuilder().create()), null);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.getFieldErrors().add(
                errorResponse.new FieldError("Unauthorized access", "unauthorized")
        );

        retrofitError = Mockito.spy(retrofitError);
        Mockito.when(retrofitError.getBodyAs(ErrorResponse.class)).thenReturn(errorResponse);

        mUserCallbackCaptor.getValue().failure(retrofitError);

        Mockito.verify(mDashboardView).launchUnauthenticatedRedirectActivity();
    }

    @Test
    public void openSessions(){
        mDashboardPresenter.openSessions();
    }

    @Test
    public void signOut(){
        mDashboardPresenter.signOut();

        Mockito.verify(mPrefManager).clearAccessToken();
        Mockito.verify(mDashboardView).showSignIn();

    }
}