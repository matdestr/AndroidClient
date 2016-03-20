package be.kdg.teame.kandoe.authentication.signin;


import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

/**
 * Interface that defines a contract between {@link SignInActivity} and {@link SignInPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface SignInContract {

    /**
     * Interface that defines the methods a View should implement
     * */
    interface View extends WebDataView {

        void showProgressIndicator(boolean active);

        /**
         * This method should be called when an attempt to login fails because the user entered wrong credentials.
         * */
        void showErrorWrongCredentials();

        /**
         * This method should be called when the {@link be.kdg.teame.kandoe.data.retrofit.AccessToken} is invalid.
         * */
        void showErrorInvalidToken();

        /**
         * This method switches to the {@link be.kdg.teame.kandoe.dashboard.DashboardActivity}.
         * */
        void showDashboard();

        /**
         * This method gets called when the user taps the sign-up button.
         * @see be.kdg.teame.kandoe.authentication.signin.SignInContract.UserActionsListener#signUp()
         * */
        void showSignUp();

    }

    /**
     * Interface that defines the methods that can be fired because of user interactions
     * */
    interface UserActionsListener extends InjectableUserActionsListener<SignInContract.View> {
        /**
         * This method gets called when the user taps the sign-in button. It connects to the server and logs the user in.
         * */
        void signIn(String username, String password);

        /**
         * This method gets called when the user taps the sign-up button. It connects to the server and creates a new account for the user.
         * */
        void signUp();
    }
}
