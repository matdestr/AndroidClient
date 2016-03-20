package be.kdg.teame.kandoe.data.retrofit.services;

import be.kdg.teame.kandoe.models.users.dto.CreateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Interface for the sign up REST API endpoint
 */

public interface SignUpService {

    /**
     * Method that does an POST HTTP call to create an user
     * @param createUserDTO DTO to create an user
     * @param callback callback function to handle the returned user object
     */
    @POST("/api/users")
    void signUp(@Body CreateUserDTO createUserDTO, Callback<User> callback);
}
