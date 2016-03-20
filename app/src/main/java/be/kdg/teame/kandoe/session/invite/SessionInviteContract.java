package be.kdg.teame.kandoe.session.invite;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

/**
 * Interface that defines a contract between {@link SessionInviteFragment} and {@link SessionInvitePresenter}.
 * This allows the view and the presenter to communicate with each other.
 */
public interface SessionInviteContract {

    /**
     * Interface that defines the methods a View should implement.
     *
     * @see AuthenticatedContract.View
     * @see WebDataView
     */
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        void showInviteFailedError(String message);
    }

    /**
     * Interface that defines the methods that can be fired because of user interactions.
     *
     * @see InjectableUserActionsListener
     * @see AuthenticatedContract.UserActionsListener
     */
    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {

        /**
         * Invites the users for a particular {@link be.kdg.teame.kandoe.models.sessions.Session}
         *
         * @param sessionId id for the session for which the users
         * @param emails    list of emails of the users who are being invited
         */
        void inviteUsers(int sessionId, List<String> emails);
    }
}
