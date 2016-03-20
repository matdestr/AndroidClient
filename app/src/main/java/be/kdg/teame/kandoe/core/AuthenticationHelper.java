package be.kdg.teame.kandoe.core;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

/**
 * This class provides a helper method to check whether the currently logged in user is authenticated.
 */
public class AuthenticationHelper {

    /**
     * This method accesses the {@link PrefManager} to retrieve the current {@link AccessToken} and checks whether this token is still valid.
     * If not valid, calls the {@link AuthenticatedContract.View#launchUnauthenticatedRedirectActivity()} method.
     * @param prefManager an instance of the {@link PrefManager}
     * @param view an instance of type {@link AuthenticatedContract.View}
     */
    public static void checkUserIsAuthenticated(PrefManager prefManager, AuthenticatedContract.View view) {
        AccessToken accessToken = prefManager.retrieveAccessToken();

        if (!accessToken.isValid())
            view.launchUnauthenticatedRedirectActivity();
    }
}
