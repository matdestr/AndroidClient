package be.kdg.teame.kandoe.core.contracts;

/**
 * Contract which provide authentication for use in MVP-pattern
 */
public interface AuthenticatedContract {

    /**
     * Interface that defines methods to redirect to an Activity if the user is not authenticated.
     * */
    interface View {
        void launchUnauthenticatedRedirectActivity();
        void launchUnauthenticatedRedirectActivity(String reason);
    }

    /**
     * Interface that defines a method to check if the user is authenticated
     * */
    interface UserActionsListener {
        /**
         * Checks if the user is authenticated.
         * */
        void checkUserIsAuthenticated();
    }

}
