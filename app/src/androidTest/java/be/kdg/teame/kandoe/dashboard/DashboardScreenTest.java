package be.kdg.teame.kandoe.dashboard;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.GravityCompat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.DaggerActivityTestRule;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.di.BaseMockDashboardPresenter;
import be.kdg.teame.kandoe.di.BaseMockSignInPresenter;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.authentication.signin.SignInContract;
import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.data.retrofit.services.SignInService;
import be.kdg.teame.kandoe.di.components.DaggerAppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.AuthenticationModule;
import be.kdg.teame.kandoe.di.modules.DashboardModule;
import be.kdg.teame.kandoe.di.modules.UserModule;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.profile.ProfileActivity;
import be.kdg.teame.kandoe.util.AssertionHelper;
import be.kdg.teame.kandoe.util.CustomViewActions;
import be.kdg.teame.kandoe.util.PreferenceHelper;
import be.kdg.teame.kandoe.util.exceptions.TokenException;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DashboardScreenTest {

    private class DashboardScreen {
        private void openDrawer() {
            onView(withId(R.id.drawer)).perform(DrawerActions.close());
            onView(withId(R.id.drawer)).perform(DrawerActions.open(GravityCompat.START));
        }

        private void signOut() {
            openDrawer();

            onView(isRoot()).perform(
                    CustomViewActions.swipeForTextToBeVisible(
                            mActivityRule.getActivity().getString(R.string.action_sign_out),
                            2000,
                            mActivityRule.getActivity().findViewById(R.id.drawer_nav_view))
            );

            onView(withText(R.string.action_sign_out)).check(matches(isDisplayed())).perform(click());
        }

        private void openProfile() {
            openDrawer();

            onView(withId(R.id.navheader_profile_pic)).perform(click());
        }

        private void signIn(String username, String password) {
            onView(withId(R.id.signin_username)).perform(typeText(username));
            onView(withId(R.id.signin_password)).perform(typeText(password));

            closeSoftKeyboard();

            onView(withId(R.id.btn_sign_in)).perform(click());
        }

    }

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityRule = new DaggerActivityTestRule<>(
            DashboardActivity.class, new DaggerActivityTestRule.OnBeforeActivityLaunchedListener<DashboardActivity>() {
        @Override
        public void beforeActivityLaunched(@NonNull Application application, @NonNull DashboardActivity activity) {
            if (mockedAuthenticationModule == null)
                mockedAuthenticationModule = new AuthenticationModule();

            if (mockedDashboardModule == null)
                mockedDashboardModule = new DashboardModule();

            ((App) application).setAppComponent(
                    DaggerAppComponent.builder()
                            .appModule(new AppModule(application))
                            .authenticationModule(mockedAuthenticationModule)
                            .dashboardModule(mockedDashboardModule)
                            .build()
            );
        }
    }
    );

    private AuthenticationModule mockedAuthenticationModule;
    private DashboardModule mockedDashboardModule;
    private User dummyUser = new User(0, "testUser", "User", "Test", "user@test.com", null);


    private void launchActivity() {
        mActivityRule.launchActivity(new Intent());
    }

    @Before
    public void setUp() {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());

        mockedAuthenticationModule = new AuthenticationModule() {
            @Override
            public SignInContract.UserActionsListener provideSignInPresenter(SignInService signInService, final PrefManager prefManager) {
                return new BaseMockSignInPresenter(signInService, prefManager) {
                    @Override
                    public void signIn(String username, String password) {
                        this.mSignInView.showDashboard();
                        AccessToken validAccessToken = new AccessToken("access-token", "token-type", "refresh-token", 1, new Date());
                        try {
                            prefManager.saveAccessToken(validAccessToken);
                            prefManager.saveUsername(dummyUser.getUsername());
                        } catch (TokenException e) {
                            //do nothing
                        }
                    }
                };
            }
        };
    }

    @Test
    public void testOpenProfile() throws Exception {
        mockedDashboardModule = new DashboardModule() {
            @Override
            public DashboardContract.UserActionsListener provideDashboardPresenter(UserService userService, final PrefManager prefManager) {
                return new BaseMockDashboardPresenter(userService, prefManager, dummyUser) {
                    @Override
                    public void openProfile() {
                        this.mDashboardView.showProfile();
                    }
                };
            }
        };

        launchActivity();

        DashboardScreen screen = new DashboardScreen();

        screen.signIn(dummyUser.getUsername(), "test");

        Thread.sleep(500);

        screen.openProfile();

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileActivity.class);
    }

    @Test
    public void testSignOut() throws Exception {
        mockedDashboardModule = new DashboardModule() {
            @Override
            public DashboardContract.UserActionsListener provideDashboardPresenter(UserService userService, final PrefManager prefManager) {
                return new BaseMockDashboardPresenter(userService, prefManager, dummyUser) {
                    @Override
                    public void signOut() {
                        prefManager.clearAccessToken();
                        this.mDashboardView.showSignIn();
                    }
                };
            }
        };

        launchActivity();

        DashboardScreen screen = new DashboardScreen();

        screen.signIn(dummyUser.getUsername(), "test");

        Thread.sleep(500);

        screen.signOut();

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignInActivity.class);

    }

    @After
    public void tearDown() {
        PreferenceManager.getDefaultSharedPreferences(getTargetContext()).edit().clear().apply();
    }
}
