package be.kdg.teame.kandoe.session;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.sessions.Session;

public interface SessionContract {

    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);
        void showSession(Session session);
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadSession(int sessionId);
    }
}
