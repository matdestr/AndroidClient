package be.kdg.teame.kandoe.data.retrofit.services;

import be.kdg.teame.kandoe.models.organizations.Organization;
import be.kdg.teame.kandoe.models.users.dto.UpdateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Interface for the {@link User} REST api endpoint
 */
public interface UserService {
    String ENDPOINT = "/api/users";

    /**
     * Proxy method that does a HTTP GET call to get an user
     * @param username username of the user
     * @param callback callback function to handle the recieved user object
     */
    @GET(ENDPOINT + "/{username}")
    void getUser(@Path("username") String username, Callback<User> callback);

    /**
     * Proxy method that does a HTTP GET call to create or register an user
     * @param userId the userId of an user
     * @param updateUserDTO the update model
     * @param callback callback function to handle the recieved user object
     */
    @PUT(ENDPOINT + "/{userId}")
    void updateUser(@Path("userId") int userId, @Body UpdateUserDTO updateUserDTO, Callback<User> callback);
}
