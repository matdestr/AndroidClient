package be.kdg.teame.kandoe.authentication.signin;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.DaggerActivityTestRule;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.di.authentication.BaseMockSignInPresenter;
import be.kdg.teame.kandoe.di.components.DaggerAppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.AuthenticationModule;
import be.kdg.teame.kandoe.util.AssertionHelper;
import be.kdg.teame.kandoe.util.PreferenceHelper;
import be.kdg.teame.kandoe.util.exceptions.TokenException;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

import static android.support.test.InstrumentationRegistry.*;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInScreenTest {
    private class SignInScreen {
        private void signIn(String username, String password) {
            onView(withId(R.id.form_username)).perform(typeText(username));
            onView(withId(R.id.form_password)).perform(typeText(password));

            closeSoftKeyboard();

            onView(withId(R.id.btn_sign_in)).perform(click());
        }
    }

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule =
            new DaggerActivityTestRule<>(
                    SignInActivity.class, true, false, new DaggerActivityTestRule.OnBeforeActivityLaunchedListener<SignInActivity>() {
                @Override
                public void beforeActivityLaunched(@NonNull Application application, @NonNull SignInActivity activity) {
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

    private AuthenticationModule mockedAuthenticationModule;

    private void launchActivity() {
        mActivityRule.launchActivity(new Intent());
    }
    @Before
    public void setUp() throws Exception {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());
    }

    @Test
    public void testSignInWrongCredentials() throws Exception {
        mockedAuthenticationModule = new AuthenticationModule() {
            @Override
            public SignInContract.UserActionsListener provideSignInPresenter(SignInService signInService, PrefManager prefManager) {
                return new BaseMockSignInPresenter(signInService, prefManager){
                    @Override
                    public void signIn(String username, String password) {
                        mSignInView.showProgressIndicator(false);
                        mSignInView.showErrorWrongCredentials();
                    }
                };
            }
        };

        launchActivity();

        SignInScreen screen = new SignInScreen();

        screen.signIn("user", "wrong-pass");

        onView(withText(R.string.sign_in_error_title)).check(matches(isDisplayed()));
    }

    @Test
    public void testSuccessfulSignIn() throws Exception {
        mockedAuthenticationModule = new AuthenticationModule() {
            @Override
            public SignInContract.UserActionsListener provideSignInPresenter(SignInService signInService, final PrefManager prefManager) {
                return new BaseMockSignInPresenter(signInService, prefManager){
                    @Override
                    public void signIn(String username, String password) {
                        AccessToken validAccessToken = new AccessToken("access-token", "token-type", "refresh-token", 1, new Date());

                        try {
                            prefManager.saveAccessToken(validAccessToken);
                        } catch (TokenException e) {
                            return;
                        }

                        prefManager.saveUsername("dummy-username");


                        mSignInView.showProgressIndicator(false);
                        mSignInView.showDashboard();
                    }
                };
            }
        };

        launchActivity();

        SignInScreen screen = new SignInScreen();

        screen.signIn("user", "pass");

        Thread.sleep(500);

        AssertionHelper.assertCurrentActivityIsInstanceOf(DashboardActivity.class);
    }

    @Test
    public void testSignUpScreenDisplays() {
        launchActivity();

        onView(withId(R.id.link_sign_up)).perform(click());
        onView(withId(R.id.btn_sign_up)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());
    }
}
