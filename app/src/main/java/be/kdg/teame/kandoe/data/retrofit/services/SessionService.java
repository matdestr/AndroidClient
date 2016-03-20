git apackage be.kdg.teame.kandoe.data.retrofit.services;

import java.util.List;

import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;
import be.kdg.teame.kandoe.models.sessions.dto.CreateReviewDTO;
import be.kdg.teame.kandoe.models.users.dto.EmailDTO;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Interface for the {@link Session} REST api endpoint
 */
public interface SessionService {
    String ENDPOINT = "/api/sessions";

    /**
     * Retrieves the sessions for the current user
     * @param callback the callback method to be called when the request is completed
     */
    @GET(ENDPOINT)
    void getSessions(Callback<List<SessionListItem>> callback);

    /**
     * Retrieves a specific session with a given id
     * @param sessionId the id of the session to retrieve
     * @param callback the callback method to be called when the request is completed
     */
    @GET(ENDPOINT + "/{sessionId}")
    void getSession(@Path("sessionId") int sessionId, Callback<Session> callback);

    /**
     * Checks whether the user is allowed to add cards to a session
     * @param sessionId the id of the session to check
     * @param callback the callback method to be called when the request is completed
     */
    @GET(ENDPOINT + "/{sessionId}/can-add-cards")
    void canUserStillAddCards(@Path("sessionId") int sessionId, Callback<Object> callback);

    /**
     * Lets the current user join a specific session
     * @param sessionId the id of the session to join
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/join")
    void join(@Path("sessionId") int sessionId, Callback<Object> callback);

    /**
     * Lets the current user decline a specific session
     * @param sessionId the id of the session to decline
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/decline")
    void decline(@Path("sessionId") int sessionId, Callback<Object> callback);

    /**
     * Retrieves all cards for a specific session
     * @param sessionId the id of the session
     * @param callback the callback method to be called when the request is completed
     */
    @GET(ENDPOINT + "/{sessionId}/all-cards")
    void getAllCards(@Path("sessionId") int sessionId, Callback<List<CardDetails>> callback);

    /**
     * Lets the user choose a specific list of cards to be used in the session
     * @param sessionId the id of the session
     * @param cardDetails a list of {@link CardDetails} ids
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/chosen-cards")
    void chooseCards(@Path("sessionId") int sessionId, @Query("cardDetailsId") List<Integer> cardDetails, Callback<Object> callback);

    /**
     * Lets the user add a list of {@link CardDetails} to a specific session
     * @param sessionId the id of the session
     * @param cardDetails a {@link CardDetails} object
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/all-cards/")
    void addCards(@Path("sessionId") int sessionId, @Body CardDetails cardDetails,Callback<Object> callback);

    /**
     * Lets the user add a list of {@link CreateReviewDTO} objects
     * @param sessionId the id of the session
     * @param reviews a list of {@link CreateReviewDTO} objects
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/reviews/add-all")
    void addReviews(@Path("sessionId") int sessionId, @Body List<CreateReviewDTO> reviews, Callback<Object> callback);

    /**
     * A call to let the server know that the user has finished adding reviews
     * @param sessionId the id of the session
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/reviews/confirm")
    void confirmReviews(@Path("sessionId") int sessionId, Callback<Object> callback);

    /**
     * A call to let the server know that the user has finished adding cards
     * @param sessionId the id of the session
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/all-cards/confirm")
    void confirmAddedCards(@Path("sessionId") int sessionId, Callback<Object> callback);

    /**
     * Starts a specific session
     * @param sessionId the id of the session
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/start")
    void start(@Path("sessionId") int sessionId, Callback<Object> callback);

    /**
     * Retrieves a list of {@link CardPosition} objects for a specific session
     * @param sessionId the id of the session
     * @param callback the callback method to be called when the request is completed
     */
    @GET(ENDPOINT + "/{sessionId}/positions")
    void getCardPositions(@Path("sessionId") int sessionId, Callback<List<CardPosition>> callback);

    /**
     * Increases the card position of a {@link CardDetails} object in a specific session
     * @param sessionId the id of the session
     * @param cardDetailsId the id of the card
     * @param callback the callback method to be called when the request is completed
     */
    @PUT(ENDPOINT + "/{sessionId}/positions")
    void increaseCardPriority(@Path("sessionId") int sessionId, @Query("cardDetailsId") int cardDetailsId, Callback<Object> callback);

    /**
     * Ends a specific session
     * @param sessionId the id of the session
     * @param callback the id of the card
     */
    @POST(ENDPOINT + "/{sessionId}/end")
    void end(@Path("sessionId") int sessionId, Callback<Object> callback);

    /**
     * Invites a list of users to a specific session
     * @param sessionId the id of the session
     * @param emails a list of {@link EmailDTO} objects
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/invite-all")
    void invite(@Path("sessionId") int sessionId, @Body List<EmailDTO> emails, Callback<Object> callback);

    /**
     * Confirms that the user accepts the invite to a specific session
     * @param sessionId the id of the session
     * @param callback the callback method to be called when the request is completed
     */
    @POST(ENDPOINT + "/{sessionId}/invite/confirm")
    void confirmInvitation(@Path("sessionId") int sessionId, Callback<Object> callback);

    /**
     * Retrieves the winning cards for a specific session
     * @param sessionId the id of the session
     * @param callback the callback method to be called when the request is completed
     */
    @GET(ENDPOINT  + "/{sessionId}/winning-cards")
    void getWinners(@Path("sessionId") int sessionId, Callback<List<CardDetails>> callback);
}
