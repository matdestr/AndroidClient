package be.kdg.teame.kandoe.dashboard;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.users.User;

/**
 * Interface that defines a contract between {@link DashboardActivity} and {@link DashboardPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface DashboardContract {

    /**
     * Interface that defines the methods a View should implement.
     * @see AuthenticatedContract.View
     * @see WebDataView
     * */
    interface View extends AuthenticatedContract.View, WebDataView {

        /**
         * Shows the specified user data to the user
         * @param user the user data
         */
        void showUserdata(User user);

        /**
         * Switches to the {@link be.kdg.teame.kandoe.authentication.signin.SignInActivity}.
         * */
        void showSignIn();

        /**
         * Switches to the {@link be.kdg.teame.kandoe.profile.ProfileActivity}.
         * */
        void showProfile();

        /**
         * Shows a list of organizations where the user is a member of
         */
        void showOrganizations();

        /**
         * Shows a list of sessions where the user can participate in
         */
        void showSessions();
    }

    /**
     * Interface that defines the methods that can be fired because of user interactions.
     * @see InjectableUserActionsListener
     * @see AuthenticatedContract.UserActionsListener
     * */
    interface UserActionsListener extends InjectableUserActionsListener<DashboardContract.View>, AuthenticatedContract.UserActionsListener {

        /**
         * Loads the currently logged in user his data
         */
        void loadUserdata();

        /**
         * Calls {@link View#showProfile()}
         */
        void openProfile();

        /**
         * Calls {@link View#showOrganizations()}
         */
        void openOrganizations();

        /**
         * Calls {@link View#showSessions()}
         */
        void openSessions();

        /**
         * Signs out the currently logged in user
         */
        void signOut();

    }
}
