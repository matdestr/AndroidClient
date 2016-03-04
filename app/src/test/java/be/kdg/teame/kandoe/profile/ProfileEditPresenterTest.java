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
import be.kdg.teame.kandoe.models.dto.UpdateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.profile.edit.ProfileEditContract;
import be.kdg.teame.kandoe.profile.edit.ProfileEditPresenter;
import be.kdg.teame.kandoe.util.http.ErrorResponse;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.http.MockResponseFactory;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

@RunWith(MockitoJUnitRunner.class)
public class ProfileEditPresenterTest {

    @Mock
    private ProfileEditContract.View mProfileEditView;

    private ProfileEditPresenter mProfileEditPresenter;

    @Mock
    private UserService mUserService;

    @Mock
    private PrefManager mPrefManager;

    @Captor
    private ArgumentCaptor<Callback<User>> mUserCallbackCaptor;

    @Before
    public void setUp() {
        mProfileEditPresenter = new ProfileEditPresenter(mUserService, mPrefManager);
        mProfileEditPresenter.setView(mProfileEditView);
    }

    @Test
    public void updateUser(){
        int userId = 0;
        String username = "user";
        String password = "pass";

        UpdateUserDTO updateUserDTO = new UpdateUserDTO(username, "firstname", "lastname", "email", password);


        mProfileEditPresenter.updateUser(userId, updateUserDTO);

        Mockito
                .verify(mUserService)
                .updateUser(Mockito.eq(userId),
                        Mockito.eq(updateUserDTO),
                        mUserCallbackCaptor.capture());

        User user = new User(userId, username, "new-firstname", "new-lastname", "new-email", "");


        mUserCallbackCaptor.getValue().success(user, null);

        Mockito.verify(mProfileEditView).showProfile();

        Mockito.verify(mPrefManager).saveUsername(Mockito.eq(user.getUsername()));
    }
}