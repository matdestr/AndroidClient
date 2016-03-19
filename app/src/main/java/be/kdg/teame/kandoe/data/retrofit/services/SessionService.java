package be.kdg.teame.kandoe.data.retrofit.services;

import java.util.List;

import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;
import be.kdg.teame.kandoe.models.users.dto.EmailDTO;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface SessionService {
    String ENDPOINT = "/api/sessions";

    @GET(ENDPOINT)
    void getSessions(Callback<List<SessionListItem>> callback);

    @GET(ENDPOINT + "/{sessionId}")
    void getSession(@Path("sessionId") int sessionId, Callback<Session> callback);

    @POST(ENDPOINT + "/{sessionId}/join")
    void join(@Path("sessionId") int sessionId, Callback<Object> callback);

    @POST(ENDPOINT + "/{sessionId}/decline")
    void decline(@Path("sessionId") int sessionId, Callback<Object> callback);

    @GET(ENDPOINT + "/{sessionId}/all-cards")
    void getAllCards(@Path("sessionId") int sessionId, Callback<List<CardDetails>> callback);

    @POST(ENDPOINT + "/{sessionId}/chosen-cards")
    void chooseCards(@Path("sessionId") int sessionId, @Query("cardDetailsId") List<Integer> cardDetails, Callback<Object> callback);

    @POST(ENDPOINT + "/{sessionId}/all-cards/addall")
    void addCards(@Path("sessionId") int sessionId, @Body List<CardDetails> cardDetails, Callback<Object> callback);

    @POST(ENDPOINT + "/{sessionId}/start")
    void start(@Path("sessionId") int sessionId, Callback<Object> callback);

    @GET(ENDPOINT + "/{sessionId}/positions")
    void getCardPositions(@Path("sessionId") int sessionId, Callback<List<CardPosition>> callback);

    @PUT(ENDPOINT + "/{sessionId}/positions")
    void increaseCardPriority(@Path("sessionId") int sessionId, @Query("cardDetailsId") int cardDetailsId, Callback<Object> callback);

    @POST(ENDPOINT + "/{sessionId}/end")
    void end(@Path("sessionId") int sessionId, Callback<Object> callback);

    @POST(ENDPOINT + "/{sessionId}/invite")
    void invite(@Path("sessionId") int sessionId, @Query("email") String email, Callback<Object> callback);

    @POST(ENDPOINT + "/{sessionId}/invite-all")
    void invite(@Path("sessionId") int sessionId, @Body List<EmailDTO> emails, Callback<Object> callback);

    @POST(ENDPOINT + "/{sessionId}/invite/confirm")
    void confirmInvitation(@Path("sessionId") int sessionId, Callback<Object> callback);

}
