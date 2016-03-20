package be.kdg.teame.kandoe.data.retrofit.services;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Interface for the sign in REST API endpoint
 */

public interface SignInService {
    String GRANT_TYPE_PASSWORD = "password";
    String GRANT_TYPE_REFRESH = "refresh_token";

    /**
     * Method that does a post HTTP call to authorize a user with the server
     * @param grantType grant type that would be used to authorize the user
     * @param username the username of the user
     * @param password the password of the user
     * @param callback the callback function to handle the recieved {@link AccessToken}
     */
    @FormUrlEncoded
    @POST("/oauth/token")
    void signIn(@Field("grant_type") String grantType,
                @Field("username") String username,
                @Field("password") String password,
                Callback<AccessToken> callback);

    /**
     * Method that does a post HTTP call to recieve a new accestoken
     * @param grantType grant type that would be used to authorize the user
     * @param refreshToken refresh token that was recieved when signing in
     * @return
     */

    @FormUrlEncoded
    @POST("/oauth/token")
    AccessToken refreshToken(@Field("grant_type") String grantType,
                             @Field("refresh_token") String refreshToken);
}
