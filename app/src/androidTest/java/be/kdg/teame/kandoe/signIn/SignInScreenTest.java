package be.kdg.teame.kandoe.signIn;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.ui.activities.SignInActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Mathisse on 19/02/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInScreenTest {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(
            SignInActivity.class);
    @Test
    public void testSignInButton() throws Exception {
        String username = "testUser";
        String password = "testPassword";

        onView(withId(R.id.signin_username)).perform(typeText(username));
        onView(withId(R.id.signin_password)).perform(typeText(password));

        closeSoftKeyboard();

        onView(withId(R.id.btn_sign_in)).perform(click());
        onView(withId(R.id.signup_username)).check(matches(isDisplayed()));
    }

    @Test
    public void testLinkToSignUp() throws Exception {
        onView(withId(R.id.link_sign_up)).perform(click());
        onView(withId(R.id.signup_username)).check(matches(isDisplayed()));
    }
}
