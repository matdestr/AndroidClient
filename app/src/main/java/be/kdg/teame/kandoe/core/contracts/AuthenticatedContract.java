package be.kdg.teame.kandoe.core.contracts;

/**
 * Contract which provide authentication for use in MVP-pattern
 */
public interface AuthenticatedContract {

    interface View {
        void launchUnauthenticatedRedirectActivity();
        void launchUnauthenticatedRedirectActivity(String reason);
    }

    interface UserActionsListener {
        void checkUserIsAuthenticated();
    }

}
