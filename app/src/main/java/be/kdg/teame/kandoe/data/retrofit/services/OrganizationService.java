package be.kdg.teame.kandoe.data.retrofit.services;

import java.util.List;

import be.kdg.teame.kandoe.models.organizations.Organization;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Interface for the {@link Organization} REST api endpoint
 */

public interface OrganizationService {
    String ENDPOINT = "/api/organizations";

    /**
     * Method that does an HTTP GET call to recieve the organization of a user
     * @param username username of the user
     * @param owner get all the organizations where to user is an owner
     * @param callback callback function for recieving the list of organizations
     */
    @GET(ENDPOINT + "/user/{username}")
    void getOrganizations(@Path("username") String username, @Query("owner") boolean owner, Callback<List<Organization>> callback);
}
