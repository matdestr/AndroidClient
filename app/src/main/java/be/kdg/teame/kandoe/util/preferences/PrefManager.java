package be.kdg.teame.kandoe.util.preferences;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;

@Singleton
public class PrefManager {
    private SharedPreferences preferences;

    @Inject
    public PrefManager(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void savePreference(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void savePreference(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void savePreference(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public String retrievePreference(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public int retrievePreference(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public AccessToken retrieveAccessToken() {
        String accessToken = retrievePreference(Preferences.Authorization.ACCESS_TOKEN, null);
        String tokenType = retrievePreference(Preferences.Authorization.TOKEN_TYPE, null);
        String refreshToken = retrievePreference(Preferences.Authorization.REFRESH_TOKEN, null);
        int expiresIn = retrievePreference(Preferences.Authorization.EXPIRES_IN, -1);

        return new AccessToken(accessToken, tokenType, refreshToken, expiresIn);
    }

    public void saveAccessToken(AccessToken accessToken) {
        savePreference(Preferences.Authorization.ACCESS_TOKEN, accessToken.getAccessToken());
        savePreference(Preferences.Authorization.TOKEN_TYPE, accessToken.getTokenType());
        savePreference(Preferences.Authorization.REFRESH_TOKEN, accessToken.getRefreshToken());
        savePreference(Preferences.Authorization.EXPIRES_IN, accessToken.getExpiresIn());
    }

    public void clearAccessToken() {
        preferences.edit()
                .remove(Preferences.Authorization.ACCESS_TOKEN)
                .remove(Preferences.Authorization.TOKEN_TYPE)
                .remove(Preferences.Authorization.REFRESH_TOKEN)
                .remove(Preferences.Authorization.EXPIRES_IN)
                .apply();
    }
}
