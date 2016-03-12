package be.kdg.teame.kandoe.data.retrofit.services;

import java.util.List;

import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface SessionService {
    String ENDPOINT = "/api/sessions";

    @GET(ENDPOINT)
    void getSessions(Callback<List<SessionListItem>> callback);

    @GET(ENDPOINT + "/{sessionId}")
    void getSession(@Path("sessionId") int sessionId, Callback<Session> callback);

    @POST(ENDPOINT + "/{sessionId}/join")
    void join(@Path("sessionId") int sessionId, Callback callback);

    @GET(ENDPOINT + "/{sessionId}/all-cards")
    void getAllCards(@Path("sessionId") int sessionId, Callback<Session> callback);

    @POST(ENDPOINT + "/{sessionId}/all-cards")
    void chooseCard(@Path("sessionId") int sessionId, Callback<Session> callback);

    @POST(ENDPOINT + "/{sessionId}/start")
    void start(@Path("sessionId") int sessionId, Callback callback);

    @GET(ENDPOINT + "/{sessionId}/positions")
    void getCardPositions(@Path("sessionId") int sessionId, Callback<List<CardPosition>> callback);

    @PUT(ENDPOINT + "/{sessionId}/positions")
    void increaseCardPriority(@Path("sessionId") int sessionId, Callback<Session> callback);

    @POST(ENDPOINT + "/{sessionId}/end")
    void end(@Path("sessionId") int sessionId, Callback callback);
}
