package be.kdg.teame.kandoe.data.retrofit.services;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface SignInService {
    String GRANT_TYPE_PASSWORD = "password";
    String GRANT_TYPE_REFRESH = "refresh_token";

    @FormUrlEncoded
    @POST("/oauth/token")
    void signIn(@Field("grant_type") String grantType,
                @Field("username") String username,
                @Field("password") String password,
                Callback<AccessToken> callback);

    @FormUrlEncoded
    @POST("/oauth/token")
    AccessToken refreshToken(@Field("grant_type") String grantType,
                             @Field("refresh_token") String refreshToken);
}
