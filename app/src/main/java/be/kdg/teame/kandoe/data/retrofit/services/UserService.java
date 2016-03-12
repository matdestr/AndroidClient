package be.kdg.teame.kandoe.data.retrofit.services;

import be.kdg.teame.kandoe.models.users.dto.UpdateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface UserService {
    String ENDPOINT = "/api/users";

    @GET(ENDPOINT + "/{username}")
    void getUser(@Path("username") String username, Callback<User> callback);

    @PUT(ENDPOINT + "/{userId}")
    void updateUser(@Path("userId") int userId, @Body UpdateUserDTO updateUserDTO, Callback<User> callback);
}
