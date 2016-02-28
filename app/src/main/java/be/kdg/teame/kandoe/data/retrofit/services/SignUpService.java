package be.kdg.teame.kandoe.data.retrofit.services;

import be.kdg.teame.kandoe.models.dto.CreateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface SignUpService {
    @POST("/api/users")
    void signUp(@Body CreateUserDTO createUserDTO, Callback<User> callback);
}
