package be.kdg.teame.kandoe.authentication.signup;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.DaggerActivityTestRule;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.authentication.MockedAppComponent;
import be.kdg.teame.kandoe.authentication.MockedAuthenticationModule;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.data.retrofit.services.SignUpService;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.di.components.DaggerAppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.AuthenticationModule;
import be.kdg.teame.kandoe.models.dto.CreateUserDTO;
import be.kdg.teame.kandoe.util.AssertionHelper;
import be.kdg.teame.kandoe.util.validators.DTOValidator;

// TODO : Check mocking

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpScreenTest {
    private class SignUpScreen {
        private void signUp(String username, String firstName, String lastName,
                            String email, String password, String verifyPassword) {
            Espresso.onView(ViewMatchers.withId(R.id.signup_username)).perform(ViewActions.typeText(username));
            Espresso.onView(ViewMatchers.withId(R.id.signup_first_name)).perform(ViewActions.typeText(firstName));
            Espresso.onView(ViewMatchers.withId(R.id.signup_last_name)).perform(ViewActions.typeText(lastName));
            Espresso.onView(ViewMatchers.withId(R.id.signup_email)).perform(ViewActions.typeText(email));
            Espresso.onView(ViewMatchers.withId(R.id.signup_password)).perform(ViewActions.typeText(password));
            Espresso.onView(ViewMatchers.withId(R.id.signup_verifypassword)).perform(ViewActions.typeText(verifyPassword));

            Espresso.closeSoftKeyboard();

            Espresso.onView(ViewMatchers.withId(R.id.btn_sign_up)).perform(ViewActions.scrollTo(), ViewActions.click());
        }
    }

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityRule = new ActivityTestRule<>(SignUpActivity.class);

    private SignUpScreen signUpScreen;

    private AppComponent mockedAppComponent;
    private MockedAuthenticationModule mockedAuthenticationModule = new MockedAuthenticationModule();

    /*@Rule
    public ActivityTestRule<SignUpActivity> mActivityRule =
            new DaggerActivityTestRule<SignUpActivity>(SignUpActivity.class, new DaggerActivityTestRule.OnBeforeActivityLaunchedListener<SignUpActivity>() {
                @Override
                public void beforeActivityLaunched(@NonNull Application application, @NonNull SignUpActivity activity) {
                    AppComponent mockedAppComponent = DaggerAppComponent
                            .builder()
                            .appModule(new AppModule(application))
                            .authenticationModule(mockedAuthenticationModule)
                            .build();

                    ((App) application).setAppComponent(mockedAppComponent);
                }
            });*/

    @Before
    public void setup() {
        signUpScreen = new SignUpScreen();
    }

    @Test
    public void successfulSignUp() {
        signUpScreen.signUp("newuser", "firstName", "lastName", "newuser@cando.com", "pass", "pass");

        /*SignUpPresenter signUpPresenter = new SignUpPresenter(null, null, null) {
            private SignUpContract.View view;

            @Override
            public void setView(SignUpContract.View view) {
                this.view = view;
            }

            public void signUp(@NonNull CreateUserDTO createUserDTO) {
                if (DTOValidator.isValid(createUserDTO)) {
                    view.showProgressIndicator(true);
                    view.showDashboard();
                }
            }
        };

        mockedAuthenticationModule.setSignUpPresenter(signUpPresenter);*/
        AssertionHelper.assertCurrentActivityIsInstanceOf(DashboardActivity.class);
    }

    @Test
    public void signUpFailedConnection() {

    }

    @Test
    public void switchToSignIn() {
        //Espresso.onView(ViewMatchers.withId(R.id.link_sign_in)).perform(ViewActions.click());
    }

    @Test
    public void signUpIncompleteUserDetails() {

    }

    @Test
    public void signUpNonMatchingPasswords() {

    }
}
