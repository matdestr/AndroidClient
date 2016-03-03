package be.kdg.teame.kandoe.core.contracts;

public interface AuthenticatedContract {

    interface View {
        void launchUnauthenticatedRedirectActivity();
        void launchUnauthenticatedRedirectActivity(String reason);
    }

    interface UserActionsListener {
        void checkUserIsAuthenticated();
    }

}
