package be.kdg.teame.kandoe.profile;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.DaggerActivityTestRule;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.di.profile.BaseMockProfileEditPresenter;
import be.kdg.teame.kandoe.di.components.DaggerAppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.UserModule;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.profile.edit.ProfileEditActivity;
import be.kdg.teame.kandoe.profile.edit.ProfileEditContract;
import be.kdg.teame.kandoe.util.AssertionHelper;
import be.kdg.teame.kandoe.util.PreferenceHelper;
import be.kdg.teame.kandoe.util.RedirectTester;
import be.kdg.teame.kandoe.util.exceptions.TokenException;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

import static android.support.test.InstrumentationRegistry.getTargetContext;

public class ProfileEditScreenTest {
    private class ProfileEditScreen {
        private void edit(String username, String firstName, String lastName,
                            String email, String verifyPassword) {
            Espresso.onView(ViewMatchers.withId(R.id.form_username))
                    .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.form_first_name))
                    .perform(ViewActions.typeText(firstName), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.form_last_name))
                    .perform(ViewActions.typeText(lastName), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.form_email))
                    .perform(ViewActions.typeText(email), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.form_verifypassword))
                    .perform(ViewActions.typeText(verifyPassword), ViewActions.closeSoftKeyboard());
            Espresso.onView(ViewMatchers.withId(R.id.fab_save)).perform(ViewActions.click());
        }

        private void clearAllFields() {
            Espresso.onView(ViewMatchers.withId(R.id.form_username))
                    .perform(ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.form_first_name))
                    .perform(ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.form_last_name))
                    .perform(ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.form_email))
                    .perform(ViewActions.clearText());
            Espresso.onView(ViewMatchers.withId(R.id.form_verifypassword))
                    .perform(ViewActions.clearText());
        }
    }


    // set launchActivity to false for intending an activity
    @Rule
    public ActivityTestRule<ProfileEditActivity> mActivityRule = new DaggerActivityTestRule<>(
            ProfileEditActivity.class, true, false, new DaggerActivityTestRule.OnBeforeActivityLaunchedListener<ProfileEditActivity>() {
        @Override
        public void beforeActivityLaunched(@NonNull Application application, @NonNull ProfileEditActivity activity) {
            if (mockedUserModule == null)
                mockedUserModule = new UserModule();

            ((App) application).setComponent(
                    DaggerAppComponent.builder()
                            .appModule(new AppModule(application))
                            .userModule(mockedUserModule)
                            .build()
            );
        }
    }
    );

    private void launchActivity() {
        Gson gson = new Gson();
        String json = gson.toJson(dummyUser);
        Intent intent = new Intent();
        intent.putExtra(ProfileEditActivity.USER, json);
        mActivityRule.launchActivity(intent);
    }

    private UserModule mockedUserModule;
    private User dummyUser = new User(0, "testUser", "User", "Test", "user@test.com", null);


    @Before
    public void setUp() {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());
    }

    @Test
    public void testEditIncompleteDetails() throws TokenException {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));

        mockedUserModule = new UserModule() {
            @Override
            public ProfileEditContract.UserActionsListener provideProfileEditPresenter(UserService userService, PrefManager prefManager) {
                return new BaseMockProfileEditPresenter(userService, prefManager);
            }
        };

        launchActivity();

        ProfileEditScreen screen = new ProfileEditScreen();
        screen.clearAllFields();

        screen.edit("newuser", "New", "User", "", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileEditActivity.class);

        screen.clearAllFields();
        screen.edit("", "New", "User", "newuser@cando.com", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileEditActivity.class);

        screen.clearAllFields();
        screen.edit("newuser", "", "User", "newuser@cando.com", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileEditActivity.class);

        screen.clearAllFields();
        screen.edit("newuser", "New", "", "newuser@cando.com", "pass");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileEditActivity.class);

        screen.clearAllFields();
        screen.edit("newuser", "New", "User", "newuser@cando.com", "");
        Espresso.onView(ViewMatchers.withText(R.string.error_field_required))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileEditActivity.class);
    }

    @Test
    public void testRedirectionToSignIn() throws Exception {
        launchActivity();

        Thread.sleep(500);

        RedirectTester.testUnauthenticatedRedirect();
    }

    @After
    public void tearDown() {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());
    }
}
