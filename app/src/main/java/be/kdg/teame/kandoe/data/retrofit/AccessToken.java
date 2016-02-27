package be.kdg.teame.kandoe.data.retrofit;

import com.google.gson.annotations.SerializedName;

import lombok.Value;

/**
 * Contains token data and bindings with GSON.
 */
@Value
public class AccessToken {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("expires_in")
    private int expiresIn;

    public String getAuthorizationHeader(){
        return tokenType + " " + accessToken;
    }
}