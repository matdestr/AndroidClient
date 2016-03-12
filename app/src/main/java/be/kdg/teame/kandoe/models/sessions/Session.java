package be.kdg.teame.kandoe.models.sessions;

import java.util.List;

import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.models.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = {"organizer", "participantInfo", "cardPositions", "sessionStatus"})
@ToString(exclude = {"organizer", "participantInfo", "cardPositions", "sessionStatus"})
public class Session {
    private int sessionId;
    private int categoryId;
    private int topicId;
    private User organizer;
    private List<ParticipantInfo> participantInfo;
    private int currentParticipantPlayingUserId;
    private List<CardPosition> cardPositions;
    private int minNumberOfCardsPerParticipant;
    private int maxNumberOfCardsPerParticipant;
    private boolean participantsCanAddCards;
    private boolean cardCommentsAllowed;
    private SessionStatus sessionStatus;
    private int amountOfCircles;
}
