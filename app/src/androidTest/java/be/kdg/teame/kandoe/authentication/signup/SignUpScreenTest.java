package be.kdg.teame.kandoe.authentication.signup;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.DaggerActivityTestRule;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.data.retrofit.services.SignUpService;
import be.kdg.teame.kandoe.di.components.DaggerAppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.AuthenticationModule;
import be.kdg.teame.kandoe.models.dto.CreateUserDTO;
import be.kdg.teame.kandoe.util.AssertionHelper;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpScreenTest {
    private class SignUpScreen {
        private void signUp(String username, String firstName, String lastName,
                            String email, String password, String verifyPassword) {
            Espresso.onView(ViewMatchers.withId(R.id.signup_username))
                    .perform(ViewActions.scrollTo(), ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.signup_first_name))
                    .perform(ViewActions.scrollTo(), ViewActions.typeText(firstName), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.signup_last_name))
                    .perform(ViewActions.scrollTo(), ViewActions.typeText(lastName), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.signup_email))
                    .perform(ViewActions.scrollTo(), ViewActions.typeText(email), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.signup_password))
                    .perform(ViewActions.scrollTo(), ViewActions.typeText(password), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.signup_verifypassword))
                    .perform(ViewActions.scrollTo(), ViewActions.typeText(verifyPassword), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.btn_sign_up)).perform(ViewActions.scrollTo(), ViewActions.click());
        }

        private void clearAllFields(){
            Espresso.onView(ViewMatchers.withId(R.id.signup_username))
                    .perform(ViewActions.scrollTo(), ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.signup_first_name))
                    .perform(ViewActions.scrollTo(), ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.signup_last_name))
                    .perform(ViewActions.scrollTo(), ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.signup_email))
                    .perform(ViewActions.scrollTo(), ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.signup_password))
                    .perform(ViewActions.scrollTo(), ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.signup_verifypassword))
                    .perform(ViewActions.scrollTo(), ViewActions.clearText());
        }
    }

    /*@Rule
    public ActivityTestRule<SignUpActivity> mActivityRule = new ActivityTestRule<>(SignUpActivity.class);*/

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityRule =
            new DaggerActivityTestRule<SignUpActivity>(
                    SignUpActivity.class, new DaggerActivityTestRule.OnBeforeActivityLaunchedListener<SignUpActivity>() {
                @Override
                public void beforeActivityLaunched(@NonNull Application application, @NonNull SignUpActivity activity) {
                    if (mockedAuthenticationModule == null)
                        mockedAuthenticationModule = new AuthenticationModule();

                    ((App) application).setAppComponent(
                            DaggerAppComponent.builder()
                                    .appModule(new AppModule(application))
                                    .authenticationModule(mockedAuthenticationModule)
                                    .build()
                    );
                }
            }
            );

    private SignUpScreen signUpScreen;

    private AuthenticationModule mockedAuthenticationModule;
    private SignUpPresenter currentSignUpPresenter;

    final String username = "newuser";
    final String firstName = "FirstName";
    final String lastName = "LastName";
    final String email = "newuser@cando.com";
    final String pass = "pass";

    private void launchActivity() {
        mActivityRule.launchActivity(new Intent());
    }

    @Before
    public void setup() {
        signUpScreen = new SignUpScreen();
        // todo remove this
        // mockedAuthenticationModule = Mockito.spy(new AuthenticationModule());
    }

    @Test
    public void successfulSignUp() throws InterruptedException {
        final CreateUserDTO createUserDTO = new CreateUserDTO(username, firstName, lastName, email, pass, pass);

        final SignUpPresenter mockedPresenter = Mockito.mock(SignUpPresenter.class);

        mockedAuthenticationModule = new AuthenticationModule() {
            @Override
            public SignUpContract.UserActionsListener provideSignUpPresenter(SignUpService signUpService, SignInService signInService, PrefManager prefManager) {
                currentSignUpPresenter = mockedPresenter;
                return mockedPresenter;
            }
        };

        launchActivity();

        signUpScreen.signUp(username, firstName, lastName, email, pass, pass);
        Mockito.verify(mockedPresenter).signUp(createUserDTO);
        mActivityRule.getActivity().showDashboard();
        Thread.sleep(500); // make sure the activity is launched before continuing checking

        AssertionHelper.assertCurrentActivityIsInstanceOf(DashboardActivity.class);
    }

    @Test
    public void switchToSignIn() {
        launchActivity();

        Espresso.onView(ViewMatchers.withId(R.id.link_sign_in)).perform(ViewActions.scrollTo(), ViewActions.click());
        AssertionHelper.assertCurrentActivityIsInstanceOf(SignInActivity.class);
    }

    @Test
    public void signUpIncompleteUserDetails() {
        launchActivity();

        signUpScreen.signUp("newuser", "New", "User", "", "pass", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);

        signUpScreen.clearAllFields();
        signUpScreen.signUp("", "New", "User", "newuser@cando.com", "pass", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);

        signUpScreen.clearAllFields();
        signUpScreen.signUp("newuser", "", "User", "newuser@cando.com", "pass", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);

        signUpScreen.clearAllFields();
        signUpScreen.signUp("newuser", "New", "", "newuser@cando.com", "pass", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);

        signUpScreen.clearAllFields();
        signUpScreen.signUp("newuser", "New", "User", "newuser@cando.com", "", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);

        signUpScreen.clearAllFields();
        signUpScreen.signUp("newuser", "New", "User", "newuser@cando.com", "pass", "");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);
    }

    @Test
    public void signUpNonMatchingPasswords() {
        launchActivity();

        signUpScreen.signUp("newuser", "New", "User", "newuser@cando.com", "pass", "differentpass");
        Espresso.onView(ViewMatchers.withText(R.string.error_verify))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);
    }

    @Test
    public void signUpWithInvalidEmail() {
        launchActivity();

        signUpScreen.signUp("newuser", "New", "User", "invalid.email", "pass", "pass");
        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);
        Espresso.onView(ViewMatchers.withText(R.string.error_invalid_email))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);
    }

    @Test
    public void signUpWithConnectionFailure() {
        mockedAuthenticationModule = new AuthenticationModule() {
            @Override
            public SignUpContract.UserActionsListener provideSignUpPresenter(SignUpService signUpService, SignInService signInService, PrefManager prefManager) {
                return new BaseMockSignUpPresenter(signUpService, signInService, prefManager) {
                    @Override
                    public void signUp(@NonNull CreateUserDTO createUserDTO) {
                        this.mSignUpView.showErrorConnectionFailure();
                    }
                };
            }
        };

        launchActivity();

        signUpScreen.signUp(username, firstName, lastName, email, pass, pass);
        Espresso.onView(ViewMatchers.withText(R.string.error_connection_failure))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignUpActivity.class);
    }
}
