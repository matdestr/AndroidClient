package be.kdg.teame.kandoe.models.sessions;

import lombok.Data;

@Data
public class SessionListItem {
    private int sessionId;
    private String organizationTitle;
    private String categoryTitle;
    private String topicTitle;
    private int participantAmount;
    private SessionStatus sessionStatus;
}
