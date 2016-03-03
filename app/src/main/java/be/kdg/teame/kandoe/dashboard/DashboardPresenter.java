package be.kdg.teame.kandoe.dashboard;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DashboardPresenter implements DashboardContract.UserActionsListener {

    private DashboardContract.View mDashboardView;
    private final UserService mUserService;
    private final PrefManager mPrefManager;

    @Inject
    public DashboardPresenter(UserService userService, PrefManager prefManager) {
        mUserService = userService;
        mPrefManager = prefManager;
    }

    @Override
    public void setView(@NonNull DashboardContract.View view) {
        mDashboardView = view;
    }

    @Override
    public void retrieveUserdata() {
        mUserService.getUser(mPrefManager.retrieveUsername(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if (user.getProfilePictureUrl() != null)
                    user.setProfilePictureUrl(Injector.getApiBaseUrl().concat("/").concat(user.getProfilePictureUrl()));

                mDashboardView.loadUserData(user);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null && error.getResponse() != null) {
                    if (error.getResponse().getStatus() == HttpStatus.UNAUTHORIZED) {
                        mDashboardView.launchUnauthenticatedRedirectActivity();
                    } else {
                        mDashboardView.showErrorConnectionFailure();
                    }
                } else {
                    mDashboardView.showErrorConnectionFailure();
                }
            }

        });
    }

    @Override
    public void openProfile() {
        mDashboardView.showProfile();
    }

    @Override
    public void signOut() {
        mPrefManager.clearAccessToken();
        mDashboardView.showSignIn();
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mDashboardView);
    }
}
