package be.kdg.teame.kandoe.data.retrofit.services;

import be.kdg.teame.kandoe.models.users.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface UserService {
    @GET("/api/users/{username}")
    void getUser(@Path("username") String username, Callback<User> callback);
}
