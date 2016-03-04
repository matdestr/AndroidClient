package be.kdg.teame.kandoe.profile;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.DaggerActivityTestRule;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.di.profile.BaseMockProfilePresenter;
import be.kdg.teame.kandoe.di.components.DaggerAppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.UserModule;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.profile.edit.ProfileEditActivity;
import be.kdg.teame.kandoe.util.AssertionHelper;
import be.kdg.teame.kandoe.util.PreferenceHelper;
import be.kdg.teame.kandoe.util.RedirectTester;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ProfileScreenTest {

    private class ProfileScreen {
        private void edit() {
            onView(withId(R.id.fab_edit)).perform(click());
        }
    }

    @Rule
    public ActivityTestRule<ProfileActivity> mActivityRule = new DaggerActivityTestRule<>(
            ProfileActivity.class, true, false, new DaggerActivityTestRule.OnBeforeActivityLaunchedListener<ProfileActivity>() {
        @Override
        public void beforeActivityLaunched(@NonNull Application application, @NonNull ProfileActivity activity) {
            if (mockedUserModule == null)
                mockedUserModule = new UserModule();

            ((App) application).setAppComponent(
                    DaggerAppComponent.builder()
                            .appModule(new AppModule(application))
                            .userModule(mockedUserModule)
                            .build()
            );
        }
    }
    );

    private void launchActivity() {
        mActivityRule.launchActivity(new Intent());
    }

    private UserModule mockedUserModule;
    private User dummyUser = new User(0, "testUser", "User", "Test", "user@test.com", null);


    @Before
    public void setUp() {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());
    }

    @Test
    public void testAuthenticatedRetrieveUserdata() throws Exception {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));

        mockedUserModule = new UserModule() {
            @Override
            public ProfileContract.UserActionsListener provideProfilePresenter(UserService userService, PrefManager prefManager) {
                return new BaseMockProfilePresenter(userService, prefManager, dummyUser);
            }
        };

        launchActivity();

        Thread.sleep(500);

        onView(ViewMatchers.withId(R.id.data_profile_username))
                .check(ViewAssertions.matches(withText(dummyUser.getUsername())));

        onView(ViewMatchers.withId(R.id.data_profile_firstname))
                .check(ViewAssertions.matches(withText(dummyUser.getFirstName())));

        onView(ViewMatchers.withId(R.id.data_profile_lastname))
                .check(ViewAssertions.matches(withText(dummyUser.getLastName())));

        onView(ViewMatchers.withId(R.id.data_profile_email))
                .check(ViewAssertions.matches(withText(dummyUser.getEmail())));

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileActivity.class);
    }

    @Test
    public void testShowEdit() throws Exception {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));

        mockedUserModule = new UserModule() {
            @Override
            public ProfileContract.UserActionsListener provideProfilePresenter(UserService userService, PrefManager prefManager) {
                return new BaseMockProfilePresenter(userService, prefManager, dummyUser);
            }
        };

        launchActivity();

        ProfileScreen screen = new ProfileScreen();
        screen.edit();

        Thread.sleep(500);

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileEditActivity.class);
    }

    @Test
    public void testRedirectionToSignIn() throws Exception {
        launchActivity();

        Thread.sleep(500);

        RedirectTester.testUnauthenticatedRedirect();
    }

    @After
    public void tearDown() throws Exception {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());
    }
}
