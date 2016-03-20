package be.kdg.teame.kandoe.dashboard.sessionlist;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;

/**
 * Interface that defines a contract between {@link SessionListFragment} and {@link SessionListPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface SessionListContract {

    /**
     * Interface that defines the methods a View should implement.
     * @see AuthenticatedContract.View
     * @see WebDataView
     * */
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        /**
         * Shows a list of {@link SessionListItem} objects to the user
         * @param sessions the list of items to show
         */
        void showSessions(List<SessionListItem> sessions);

        /**
         * Shows an error message stating no sessions where found or hides this message and shows the sessions.
         * @param active boolean that indicates whether the error message should be shown or hidden
         */
        void showZeroSessionsFoundMessage(boolean active);

        /**
         * Shows the details of a {@link SessionListItem} object.
         * @param session the session to show
         */
        void showSessionDetail(SessionListItem session);
    }

    /**
     * Interface that defines the methods that can be fired because of user interactions.
     * @see InjectableUserActionsListener
     * @see AuthenticatedContract.UserActionsListener
     * */
    interface UserActionsListener extends InjectableUserActionsListener<SessionListContract.View>, AuthenticatedContract.UserActionsListener {
        /**
         * Loads a list of session where the current user can participate in and notifies the View by calling {@link View#showSessions(List)}
         */
        void loadSessions();

        /**
         * Calls {@link View#showSessionDetail(SessionListItem)} to show the details of this session
         * @param session the session to show
         */
        void openSession(SessionListItem session);
    }
}
