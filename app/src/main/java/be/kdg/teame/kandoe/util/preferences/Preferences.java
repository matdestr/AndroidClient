package be.kdg.teame.kandoe.util.preferences;

public interface Preferences {
    interface Authorization {
        String ACCESS_TOKEN = "PREF_AUTHORIZATION_ACCESS_TOKEN";
        String TOKEN_TYPE = "PREF_AUTHORIZATION_TOKEN_TYPE";
        String REFRESH_TOKEN = "PREF_AUTHORIZATION_REFRESH_TOKEN";
        String EXPIRES_IN = "PREF_AUTHORIZATION_EXPIRES_IN";
        String DATE_ACQUIRED ="PREF_DATE_ACQUIRED";
    }

    /**
     * Shared preference which tracks whether or not
     * the user has learned to use the drawer manually.
     */
    String USER_LEARNED_DRAWER = "PREF_USER_LEARNED_DRAWER";
}
