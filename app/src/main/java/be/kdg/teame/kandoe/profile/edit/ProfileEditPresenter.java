package be.kdg.teame.kandoe.profile.edit;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.models.users.dto.UpdateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileEditPresenter implements ProfileEditContract.UserActionsListener {

    private ProfileEditContract.View mProfileEditView;
    private final UserService mUserService;
    private final PrefManager mPrefManager;

    @Inject
    public ProfileEditPresenter(UserService userService, PrefManager prefManager) {
        mUserService = userService;
        mPrefManager = prefManager;
    }

    @Override
    public void setView(@NonNull ProfileEditContract.View view) {
        mProfileEditView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mProfileEditView);
    }

    @Override
    public void updateUser(int userId, UpdateUserDTO updateUserDTO) {
        mUserService.updateUser(userId, updateUserDTO, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                mPrefManager.saveUsername(user.getUsername());
                mProfileEditView.showProfile();
            }

            @Override
            public void failure(RetrofitError error) {
                mProfileEditView.showErrorConnectionFailure("Couldn't update user information.");
            }
        });
    }
}
