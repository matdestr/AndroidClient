package be.kdg.teame.kandoe.data.retrofit.services;

import java.util.List;

import be.kdg.teame.kandoe.models.cards.CardDetails;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface CardDetailsService {
    String ENDPOINT = "/api/carddetails";

    @GET(ENDPOINT + "/topics/{topicId}")
    void getCardDetailsOfTopic(@Path("topicId") int topicId, Callback<List<CardDetails>> cardDetails);

    @GET(ENDPOINT + "/categories/{categoryId}")
    void getCardDetailsOfCategory(@Path("categoryId") int categoryId, Callback<List<CardDetails>> cardDetails);

    @POST(ENDPOINT + "/topics")
    void addCardDetailsToTopic(@Query("topicId") int topicId, @Query("cardDetailsId") int cardDetailsId, Callback<List<CardDetails>> cardDetails);


}
