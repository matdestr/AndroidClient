package be.kdg.teame.kandoe.di.profile;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.profile.ProfileContract;
import be.kdg.teame.kandoe.profile.ProfilePresenter;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class BaseMockProfilePresenter extends ProfilePresenter {
    public ProfileContract.View mProfileView;
    private User mUser;


    public BaseMockProfilePresenter(UserService userService, PrefManager prefManager, User user) {
        super(userService, prefManager);
        mUser = user;
    }

    @Override
    public void loadUserdata() {
        this.mProfileView.showUserdata(mUser);
    }

    @Override
    public void openEditMode() {
        this.mProfileView.showEdit(mUser);
    }

    @Override
    public void setView(@NonNull ProfileContract.View view) {
        mProfileView = view;
    }
}
