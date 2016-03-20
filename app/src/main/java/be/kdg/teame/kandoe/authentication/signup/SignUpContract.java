package be.kdg.teame.kandoe.authentication.signup;

import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.users.dto.CreateUserDTO;

/**
 * Interface that defines a contract between {@link SignUpActivity} and {@link SignUpPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface SignUpContract {
    /**
     * Interface that defines the methods a View should implement.
     * */
    interface View {
        /**
         * Shows or hides a dialog that indicates the progress of an action.
         * @param active boolean that indicates wheter the dialog should show or hide.
         * */
        void showProgressIndicator(boolean active);

        /**
         * Shows an error dialog indicating that there was a connection failure.
         * */
        void showErrorConnectionFailure();

        /**
         * Switches to the {@link be.kdg.teame.kandoe.authentication.signin.SignInActivity}.
         * */
        void showSignIn();

        /**
         * Shows an error dialog if the automatic sign-in after registering fails.
         */
        void showErrorAutomaticSignInFailure();

        /**
         * Shows an error dialog if the user entered incomplete details.
         */
        void showErrorIncompleteDetails();

        /**
         * Shows an error dialog if the passwords entered don't match.
         */
        void showErrorNonMatchingPasswordFields();

        /**
         * Shows an error dialog if the creation of a user failed.
         */
        void showErrorUserCreation();

        /**
         * Shows an error dialog with a given server message.
         * @param reason the message from the server
         */
        void showErrorServerMessage(String reason);

        /**
         * Shows an error dialog if the {@link be.kdg.teame.kandoe.data.retrofit.AccessToken} is invalid.
         */
        void showErrorInvalidToken();

        /**
         * This method switches to the {@link be.kdg.teame.kandoe.dashboard.DashboardActivity}.
         * */
        void showDashboard();
    }

    /**
     * Interface that defines the methods that can be fired because of user interactions.
     * @see InjectableUserActionsListener
     * */
    interface UserActionsListener extends InjectableUserActionsListener<SignUpContract.View> {
        /**
         * This method calls the {@link View#showSignIn()} method.
         */
        void signIn();

        /**
         * This method send a {@link CreateUserDTO} to the server and waits for its response.
         * @param createUserDTO
         */
        void signUp(CreateUserDTO createUserDTO);
    }
}
