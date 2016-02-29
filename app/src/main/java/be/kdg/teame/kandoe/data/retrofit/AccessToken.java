package be.kdg.teame.kandoe.data.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

/**
 * Contains token data and bindings with GSON.
 */
@Data
@AllArgsConstructor
public class AccessToken {
    @Setter(AccessLevel.NONE)
    @SerializedName("access_token")
    private String accessToken;

    @Setter(AccessLevel.NONE)
    @SerializedName("token_type")
    private String tokenType;

    @Setter(AccessLevel.NONE)
    @SerializedName("refresh_token")
    private String refreshToken;

    @Setter(AccessLevel.NONE)
    @SerializedName("expires_in")
    private long expiresIn;

    private Date dateAcquired;

    public String getAuthorizationHeader() {
        return tokenType + " " + accessToken;
    }

    public boolean isValid() {
        return !(accessToken == null || accessToken.isEmpty() ||
                tokenType == null || tokenType.isEmpty() ||
                refreshToken == null || refreshToken.isEmpty() ||
                expiresIn < 0 || dateAcquired == null);
    }

    public boolean isExpired() {
        Calendar currentDate = Calendar.getInstance();
        return TimeUnit.MILLISECONDS.toSeconds(dateAcquired.getTime()) + expiresIn <
                TimeUnit.MILLISECONDS.toSeconds(currentDate.getTimeInMillis());
    }
}