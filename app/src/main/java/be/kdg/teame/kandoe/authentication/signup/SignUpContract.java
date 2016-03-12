package be.kdg.teame.kandoe.authentication.signup;

import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.models.users.dto.CreateUserDTO;

public interface SignUpContract {
    interface View {
        void showProgressIndicator(boolean active);

        void showErrorConnectionFailure();

        void showSignIn();

        void showErrorAutomaticSignInFailure();

        void showErrorIncompleteDetails();

        void showErrorNonMatchingPasswordFields();

        void showErrorUserCreation();

        void showErrorServerMessage(String reason);

        void showErrorInvalidToken();

        void showDashboard();
    }

    interface UserActionsListener extends InjectableUserActionsListener<SignUpContract.View> {
        void signIn();

        void signUp(CreateUserDTO createUserDTO);
    }
}
