package be.kdg.teame.kandoe.util.preferences;

public interface Preferences {
    interface Authorization {
        String ACCESS_TOKEN = "AUTHORIZATION_ACCESS_TOKEN";
        String TOKEN_TYPE = "AUTHORIZATION_TOKEN_TYPE";
        String REFRESH_TOKEN = "AUTHORIZATION_REFRESH_TOKEN";
        String EXPIRES_IN = "AUTHORIZATION_EXPIRES_IN";
    }
}
