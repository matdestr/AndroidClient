package be.kdg.teame.kandoe.di.profile;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.models.users.dto.UpdateUserDTO;
import be.kdg.teame.kandoe.profile.edit.ProfileEditContract;
import be.kdg.teame.kandoe.profile.edit.ProfileEditPresenter;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class BaseMockProfileEditPresenter extends ProfileEditPresenter {
    public ProfileEditContract.View mProfileEditView;

    public BaseMockProfileEditPresenter(UserService userService, PrefManager prefManager) {
        super(userService, prefManager);
    }

    @Override
    public void updateUser(int userId, UpdateUserDTO updateUserDTO) {
        mProfileEditView.showProfile();
    }

    @Override
    public void setView(@NonNull ProfileEditContract.View view) {
        mProfileEditView = view;
    }
}
