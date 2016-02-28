package be.kdg.teame.kandoe.util.preferences;

public interface Preferences {
    interface Authorization {
        String ACCESS_TOKEN = "AUTHORIZATION_ACCESS_TOKEN";
        String TOKEN_TYPE = "AUTHORIZATION_TOKEN_TYPE";
        String REFRESH_TOKEN = "AUTHORIZATION_REFRESH_TOKEN";
        String EXPIRES_IN = "AUTHORIZATION_EXPIRES_IN";
    }

    /**
     * Shared preference which tracks whether or not
     * the user has learned to use the drawer manually.
     */
    String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
}
