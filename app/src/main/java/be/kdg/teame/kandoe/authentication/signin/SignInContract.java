package be.kdg.teame.kandoe.authentication.signin;


import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

public interface SignInContract {

    interface View extends WebDataView {

        void showProgressIndicator(boolean active);

        void showErrorWrongCredentials();

        void showErrorInvalidToken();

        void showDashboard();

        void showSignUp();

    }

    interface UserActionsListener extends InjectableUserActionsListener<SignInContract.View> {
        void signIn(String username, String password);

        void signUp();
    }
}
