package be.kdg.teame.kandoe.util.http;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;

public class MockTokenFactory {
    public static AccessToken getMockAccessToken() {
        return new AccessToken("dummy accessToken", "dummy tokenType", "dummy refreshToken", -1);
    }
}
