package be.kdg.teame.kandoe.util;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RedirectTester {
    public static void testUnauthenticatedRedirect(){
        AssertionHelper.assertCurrentActivityIsInstanceOf(SignInActivity.class);

        onView(withId(R.id.form_username)).check(matches(isDisplayed()));
        onView(withId(R.id.form_password)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_sign_in)).check(matches(isDisplayed()));
    }
}
