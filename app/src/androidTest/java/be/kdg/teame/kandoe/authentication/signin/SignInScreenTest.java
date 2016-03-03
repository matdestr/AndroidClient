package be.kdg.teame.kandoe.authentication.signin;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.dashboard.DashboardActivity;
import be.kdg.teame.kandoe.util.AssertionHelper;
import be.kdg.teame.kandoe.util.PreferenceHelper;

import static android.support.test.InstrumentationRegistry.*;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
            onView(withId(R.id.signin_username)).perform(typeText(username));
            onView(withId(R.id.signin_password)).perform(typeText(password));

            closeSoftKeyboard();

            onView(withId(R.id.btn_sign_in)).perform(click());
        }
    }

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void testSignInWrongCredentials() throws Exception {
        SignInScreen screen = new SignInScreen();

        screen.signIn("user", "wrong-pass");

        onView(withText(R.string.sign_in_error_title)).check(matches(isDisplayed()));
    }

    @Test
    public void testSuccessfulSignIn() throws Exception {
        SignInScreen screen = new SignInScreen();

        screen.signIn("user", "pass");

        AssertionHelper.assertCurrentActivityIsInstanceOf(DashboardActivity.class);
    }

    @Test
    public void testSignUpScreenDisplays() {
        onView(withId(R.id.link_sign_up)).perform(click());
        onView(withId(R.id.signup_username)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());
    }
}
