package be.kdg.teame.kandoe.profile;

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

public class ProfilePresenter implements ProfileContract.UserActionsListener {

    private ProfileContract.View mProfileView;
    private final UserService mUserService;
    private final PrefManager mPrefManager;
    private User mUser;

    @Inject
    public ProfilePresenter(UserService userService, PrefManager prefManager) {
        mUserService = userService;
        mPrefManager = prefManager;
    }

    @Override
    public void setView(@NonNull ProfileContract.View view) {
        mProfileView = view;
    }

    @Override
    public void loadUserdata() {
        mProfileView.showRetrievingDataStatus();

        mUserService.getUser(mPrefManager.retrieveUsername(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if (user.getProfilePictureUrl() != null)
                    user.setProfilePictureUrl(Injector.getApiBaseUrl().concat("/").concat(user.getProfilePictureUrl()));

                mUser = user;

                mProfileView.showUserdata(mUser);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null && error.getResponse() != null) {
                    if (error.getResponse().getStatus() == HttpStatus.BAD_REQUEST || error.getResponse().getStatus() == HttpStatus.UNAUTHORIZED) {
                        mProfileView.launchUnauthenticatedRedirectActivity();
                    } else {
                        mProfileView.showErrorConnectionFailure("Unable to retrieve profile information for " + mPrefManager.retrieveUsername());
                    }
                } else {
                    mProfileView.showErrorConnectionFailure(null);
                }
            }
        });
    }

    @Override
    public void openEditMode() {
        if (mUser != null)
            mProfileView.showEdit(mUser);
        else {
            loadUserdata();
        }

    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mProfileView);
    }
}
