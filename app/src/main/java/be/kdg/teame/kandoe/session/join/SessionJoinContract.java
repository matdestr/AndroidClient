package be.kdg.teame.kandoe.session.join;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.sessions.Session;

/**
 * Interface that defines a contract between {@link SessionJoinFragment} and {@link SessionJoinPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface SessionJoinContract {

    /**
     * Interface that defines the methods a View should implement
     * @see WebDataView
     * */
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);
        void close();

        void showJoined();

        void showUserJoined();
    }

    /**
     * Interface that defines a contract between {@link SessionJoinFragment} and {@link SessionJoinPresenter}.
     * This allows the view and the presenter to communicate with each other.
     * */
    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void join(int sessionId);

        void decline(int sessionId);
    }
}
