package be.kdg.teame.kandoe.profile;

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
public class ProfilePresenterTest {

    @Mock
    private ProfileContract.View mProfileView;

    private ProfilePresenter mProfilePresenter;

    @Mock
    private UserService mUserService;

    @Mock
    private PrefManager mPrefManager;

    @Captor
    private ArgumentCaptor<Callback<User>> mUserCallbackCaptor;

    @Before
    public void setUp() {
        mProfilePresenter = new ProfilePresenter(mUserService, mPrefManager);
        mProfilePresenter.setView(mProfileView);
    }

    @Test
    public void authenticatedAccess(){
        AccessToken validAccessToken = new AccessToken("access-token", "token-type", "refresh-token", 1, new Date());

        Mockito.when(mPrefManager.retrieveAccessToken()).thenReturn(validAccessToken);

        mProfilePresenter.checkUserIsAuthenticated();

        Mockito.verifyZeroInteractions(mProfileView);
    }

    @Test
    public void unauthenticatedAccess(){
        AccessToken invalidAccessToken = new AccessToken(null, null, null, -1, null);

        Mockito.when(mPrefManager.retrieveAccessToken()).thenReturn(invalidAccessToken);

        mProfilePresenter.checkUserIsAuthenticated();

        Mockito.verify(mProfileView).launchUnauthenticatedRedirectActivity();
    }

    @Test
    public void authenticatedRetrieveUserdata(){
        Mockito.when(mPrefManager.retrieveUsername()).thenReturn("username");

        mProfilePresenter.loadUserdata();

        Mockito.verify(mUserService)
                .getUser(Mockito.eq("username"), mUserCallbackCaptor.capture());

        User user = new User(0, "username", "firstname", "lastname", "user@cando.com", "");

        mUserCallbackCaptor.getValue().success(user, null);

        Mockito.verify(mProfileView).showRetrievingDataStatus();
        Mockito.verify(mProfileView).showUserdata(Mockito.eq(user));
    }

    @Test
    public void unauthenticatedRetrieveUserdata() throws JSONException {
        Mockito.when(mPrefManager.retrieveUsername()).thenReturn("username");

        mProfilePresenter.loadUserdata();

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

        Mockito.verify(mProfileView).launchUnauthenticatedRedirectActivity();
    }

    //todo check better way
    //@Test
    public void editProfile(){
        mProfilePresenter.openEditMode();

        Mockito.verify(mProfileView).showEdit(null);

    }
}