package be.kdg.teame.kandoe.data.retrofit.services;

import java.util.List;

import be.kdg.teame.kandoe.models.organizations.Organization;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface OrganizationService {
    String ENDPOINT = "/api/organizations";

    @GET(ENDPOINT + "/user/{username}")
    void getOrganizations(@Path("username") String username, @Query("owner") boolean owner, Callback<List<Organization>> callback);



}
