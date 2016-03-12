package be.kdg.teame.kandoe.dashboard.sessionlist;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;

public interface SessionListContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        void showSessions(List<SessionListItem> sessions);

        void showZeroSessionsFoundMessage(boolean active);

        void showSessionDetail(int sessionId);
    }

    interface UserActionsListener extends InjectableUserActionsListener<SessionListContract.View>, AuthenticatedContract.UserActionsListener {
        void loadSessions();

        void openSession(SessionListItem session);
    }
}
