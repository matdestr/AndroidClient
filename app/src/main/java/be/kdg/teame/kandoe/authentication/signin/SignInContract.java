package be.kdg.teame.kandoe.authentication.signin;


import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;

public interface SignInContract {

    interface View {

        void showProgressIndicator(boolean active);

        void showErrorWrongCredentials();

        void showErrorConnectionFailure();

        void showErrorInvalidToken();

        void showDashboard();

        void showSignUp();

    }

    interface UserActionsListener extends InjectableUserActionsListener<SignInContract.View> {
        void signIn(String username, String password);

        void signUp();
    }
}
