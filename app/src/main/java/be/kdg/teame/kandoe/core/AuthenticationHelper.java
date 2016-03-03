package be.kdg.teame.kandoe.core;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class AuthenticationHelper {
    public static void checkUserIsAuthenticated(PrefManager prefManager, AuthenticatedContract.View view) {
        AccessToken accessToken = prefManager.retrieveAccessToken();

        if (!accessToken.isValid())
            view.launchUnauthenticatedRedirectActivity();
    }
}
