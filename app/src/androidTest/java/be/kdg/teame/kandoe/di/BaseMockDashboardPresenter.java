package be.kdg.teame.kandoe.di;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.dashboard.DashboardContract;
import be.kdg.teame.kandoe.dashboard.DashboardPresenter;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class BaseMockDashboardPresenter extends DashboardPresenter {
    public DashboardContract.View mDashboardView;
    private User mUser;

    public BaseMockDashboardPresenter(UserService userService, PrefManager prefManager, User user) {
        super(userService, prefManager);
        mUser = user;
    }

    @Override
    public void retrieveUserdata() {
        this.mDashboardView.loadUserData(mUser);
    }

    @Override
    public void setView(@NonNull DashboardContract.View view) {
        mDashboardView = view;
    }
}
