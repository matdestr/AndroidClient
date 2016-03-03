package be.kdg.teame.kandoe.util.preferences;

import android.content.SharedPreferences;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.util.exceptions.TokenException;

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

    public void savePreference(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public void savePreference(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public String retrievePreference(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public boolean retrievePreference(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public int retrievePreference(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public long retrievePreference(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public String retrieveUsername(){
        return retrievePreference(Preferences.User.USERNAME, null);
    }

    public void saveUsername(String username){
        savePreference(Preferences.User.USERNAME, username);
    }

    public void clearUsername(){
        preferences.edit().remove(Preferences.User.USERNAME).apply();
    }

    public AccessToken retrieveAccessToken() {
        String accessToken = retrievePreference(Preferences.Authorization.ACCESS_TOKEN, null);
        String tokenType = retrievePreference(Preferences.Authorization.TOKEN_TYPE, null);
        String refreshToken = retrievePreference(Preferences.Authorization.REFRESH_TOKEN, null);
        long expiresIn = retrievePreference(Preferences.Authorization.EXPIRES_IN, -1l);
        long dateAcquired = retrievePreference(Preferences.Authorization.DATE_ACQUIRED, -1l);

        return new AccessToken(accessToken, tokenType, refreshToken, expiresIn, new Date(dateAcquired));
    }

    public void saveAccessToken(AccessToken accessToken) throws TokenException {
        if (!accessToken.isValid()) throw new TokenException("token is invalid");
        if (accessToken.isExpired()) throw new TokenException("token is expired");

        savePreference(Preferences.Authorization.ACCESS_TOKEN, accessToken.getAccessToken());
        savePreference(Preferences.Authorization.TOKEN_TYPE, accessToken.getTokenType());
        savePreference(Preferences.Authorization.REFRESH_TOKEN, accessToken.getRefreshToken());
        savePreference(Preferences.Authorization.EXPIRES_IN, accessToken.getExpiresIn());
        savePreference(Preferences.Authorization.DATE_ACQUIRED, accessToken.getDateAcquired().getTime());
    }

    public void clearAccessToken() {
        preferences.edit()
                .remove(Preferences.Authorization.ACCESS_TOKEN)
                .remove(Preferences.Authorization.TOKEN_TYPE)
                .remove(Preferences.Authorization.REFRESH_TOKEN)
                .remove(Preferences.Authorization.EXPIRES_IN)
                .remove(Preferences.Authorization.DATE_ACQUIRED)
                .apply();

        clearUsername();
    }
}
