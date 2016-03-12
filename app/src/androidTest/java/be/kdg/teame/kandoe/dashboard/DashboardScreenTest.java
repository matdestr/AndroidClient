package be.kdg.teame.kandoe.dashboard;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.DaggerActivityTestRule;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.dashboard.organizationlist.OrganizationListContract;
import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListContract;
import be.kdg.teame.kandoe.data.retrofit.services.OrganizationService;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.di.BaseMockDashboardPresenter;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.data.retrofit.AccessToken;
import be.kdg.teame.kandoe.di.BaseMockOrganizationListPresenter;
import be.kdg.teame.kandoe.di.components.DaggerAppComponent;
import be.kdg.teame.kandoe.di.modules.AppModule;
import be.kdg.teame.kandoe.di.modules.DashboardModule;
import be.kdg.teame.kandoe.di.modules.OrganizationModule;
import be.kdg.teame.kandoe.di.modules.SessionModule;
import be.kdg.teame.kandoe.di.session.BaseMockSessionListPresenter;
import be.kdg.teame.kandoe.models.organizations.Organization;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;
import be.kdg.teame.kandoe.models.sessions.SessionStatus;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.profile.ProfileActivity;
import be.kdg.teame.kandoe.util.AssertionHelper;
import be.kdg.teame.kandoe.util.CustomViewActions;
import be.kdg.teame.kandoe.util.PreferenceHelper;
import be.kdg.teame.kandoe.util.RedirectTester;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static be.kdg.teame.kandoe.util.CustomMatchers.*;
import static be.kdg.teame.kandoe.util.CustomViewActions.withCustomConstraints;
import static org.hamcrest.Matchers.*;

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

        private void openOrganizations() {
            openDrawer();

            onView(isRoot()).perform(
                    CustomViewActions.swipeForTextToBeVisible(
                            mActivityRule.getActivity().getString(R.string.action_organizations),
                            2000,
                            mActivityRule.getActivity().findViewById(R.id.drawer_nav_view))
            );

            onView(withText(R.string.action_organizations)).check(matches(isDisplayed())).perform(click());
        }

        private void openSessions() {
            openDrawer();

            onView(isRoot()).perform(
                    CustomViewActions.swipeForTextToBeVisible(
                            mActivityRule.getActivity().getString(R.string.action_sessions),
                            2000,
                            mActivityRule.getActivity().findViewById(R.id.drawer_nav_view))
            );

            onView(withText(R.string.action_sessions)).check(matches(isDisplayed())).perform(click());
        }

        private void refresh() {
            onView(withId(R.id.refresh_layout))
                    .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));
        }
    }

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityRule = new DaggerActivityTestRule<>(
            DashboardActivity.class, true, false, new DaggerActivityTestRule.OnBeforeActivityLaunchedListener<DashboardActivity>() {
        @Override
        public void beforeActivityLaunched(@NonNull Application application, @NonNull DashboardActivity activity) {

            if (mockedDashboardModule == null)
                mockedDashboardModule = new DashboardModule();

            if (mockedOrganizationModule == null)
                mockedOrganizationModule = new OrganizationModule();

            if (mockedSessionModule == null)
                mockedSessionModule = new SessionModule();

            ((App) application).setComponent(
                    DaggerAppComponent.builder()
                            .appModule(new AppModule(application))
                            .dashboardModule(mockedDashboardModule)
                            .organizationModule(mockedOrganizationModule)
                            .sessionModule(mockedSessionModule)
                            .build()
            );
        }
    }
    );

    private DashboardModule mockedDashboardModule;
    private OrganizationModule mockedOrganizationModule;
    private SessionModule mockedSessionModule;
    private User dummyUser = new User(0, "testUser", "User", "Test", "user@test.com", null);
    private Organization testOrganization = new Organization();
    SessionListItem sessionListItem1 = new SessionListItem();

    SessionListItem sessionListItem2 = new SessionListItem();

    SessionListItem sessionListItem3 = new SessionListItem();


    private void launchActivity() {
        mActivityRule.launchActivity(new Intent());
    }

    @Before
    public void setUp() throws Exception {
        PreferenceHelper.clearAllSharedPreferences(getTargetContext());

        testOrganization.setName("Test Organization");

        sessionListItem1.setSessionId(1);
        sessionListItem1.setSessionStatus(SessionStatus.CREATED);

        sessionListItem2.setSessionId(2);
        sessionListItem2.setSessionStatus(SessionStatus.READY_TO_START);

        sessionListItem3.setSessionId(3);
        sessionListItem3.setSessionStatus(SessionStatus.FINISHED);

        mockedDashboardModule = new DashboardModule() {
            @Override
            public DashboardContract.UserActionsListener provideDashboardPresenter(UserService userService, final PrefManager prefManager) {
                return new BaseMockDashboardPresenter(userService, prefManager, dummyUser) {
                    @Override
                    public void openOrganizations() {
                        mDashboardView.showOrganizations();
                    }

                    @Override
                    public void openSessions() {
                        mDashboardView.showSessions();
                    }

                    @Override
                    public void checkUserIsAuthenticated() {
                        AuthenticationHelper.checkUserIsAuthenticated(prefManager, mDashboardView);
                    }
                };
            }
        };

        mockedOrganizationModule = new OrganizationModule() {
            @Override
            public OrganizationListContract.UserActionsListener provideOrganizationListPresenter(OrganizationService organizationService, PrefManager prefManager) {
                return new BaseMockOrganizationListPresenter(organizationService, prefManager) {
                    @Override
                    public void loadOrganizations() {
                        mOrganizationListView.setProgressIndicator(false);
                        //do nothing
                    }
                };
            }
        };
    }

    @Test
    public void testOpenProfile() throws Exception {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));

        mockedDashboardModule = new DashboardModule() {
            @Override
            public DashboardContract.UserActionsListener provideDashboardPresenter(UserService userService, final PrefManager prefManager) {
                return new BaseMockDashboardPresenter(userService, prefManager, dummyUser) {

                    @Override
                    public void openOrganizations() {
                        mDashboardView.showOrganizations();
                    }

                    @Override
                    public void openProfile() {
                        this.mDashboardView.showProfile();
                    }
                };
            }
        };

        launchActivity();

        DashboardScreen screen = new DashboardScreen();

        Thread.sleep(500);

        screen.openProfile();

        AssertionHelper.assertCurrentActivityIsInstanceOf(ProfileActivity.class);
    }

    @Test
    public void testOpenOrganizations() throws Exception {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));


        mockedOrganizationModule = new OrganizationModule() {
            @Override
            public OrganizationListContract.UserActionsListener provideOrganizationListPresenter(OrganizationService organizationService, PrefManager prefManager) {
                return new BaseMockOrganizationListPresenter(organizationService, prefManager) {
                    @Override
                    public void loadOrganizations() {
                        mOrganizationListView.showZeroOrganizationsFoundMessage(false);
                        mOrganizationListView.setProgressIndicator(true);

                        List<Organization> items = new ArrayList<>();

                        items.add(testOrganization);

                        mOrganizationListView.setProgressIndicator(false);
                        mOrganizationListView.showOrganizations(items);
                    }
                };
            }
        };

        launchActivity();
        DashboardScreen screen = new DashboardScreen();

        Thread.sleep(500);

        screen.openOrganizations();

        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(
                        is(getTargetContext()
                                .getResources()
                                .getString(R.string.organizations_label)))));

        onView(withId(R.id.refresh_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withText(testOrganization.getName())).check(matches(isDisplayed()));

    }

    @Test
    public void testOpenOrganizationsWhenZeroOrganizationsExist() throws Exception {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));

        mockedOrganizationModule = new OrganizationModule() {
            @Override
            public OrganizationListContract.UserActionsListener provideOrganizationListPresenter(OrganizationService organizationService, PrefManager prefManager) {
                return new BaseMockOrganizationListPresenter(organizationService, prefManager) {
                    @Override
                    public void loadOrganizations() {
                        mOrganizationListView.showZeroOrganizationsFoundMessage(false);
                        mOrganizationListView.setProgressIndicator(true);

                        mOrganizationListView.setProgressIndicator(false);
                        mOrganizationListView.showZeroOrganizationsFoundMessage(true);
                    }
                };
            }
        };

        launchActivity();
        DashboardScreen screen = new DashboardScreen();

        Thread.sleep(500);

        screen.openOrganizations();

        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(
                        is(getTargetContext()
                                .getResources()
                                .getString(R.string.organizations_label)))));

        onView(withId(R.id.refresh_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.nothing_to_show_container)).check(matches(isDisplayed()));
        onView(withText(R.string.nothing_to_show_organizations)).check(matches(isDisplayed()));
    }

    @Test
    public void testOpenSessions() throws Exception {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));

        mockedSessionModule = new SessionModule() {
            @Override
            public SessionListContract.UserActionsListener provideSessionListPresenter(SessionService sessionService, PrefManager prefManager) {
                return new BaseMockSessionListPresenter(sessionService, prefManager) {
                    @Override
                    public void loadSessions() {
                        mSessionListView.setProgressIndicator(true);
                        List<SessionListItem> items = new ArrayList<>();

                        items.add(sessionListItem1);
                        items.add(sessionListItem2);
                        items.add(sessionListItem3);

                        mSessionListView.setProgressIndicator(false);

                        mSessionListView.showSessions(items);
                    }
                };
            }
        };

        launchActivity();
        DashboardScreen screen = new DashboardScreen();

        Thread.sleep(500);

        screen.openSessions();

        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(
                        is(getTargetContext()
                                .getResources()
                                .getString(R.string.sessions_label)))));

        onView(withId(R.id.refresh_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.nothing_to_show_container)).check(matches(isDisplayed()));
        onView(withText("Session " + sessionListItem1.getSessionId())).check(matches(isDisplayed()));
        onView(withText("Session " + sessionListItem2.getSessionId())).check(matches(isDisplayed()));
        onView(withText("Session " + sessionListItem3.getSessionId())).check(matches(isDisplayed()));


    }

    @Test
    public void testOpenSessionsWhenZeroSessionsExist() throws Exception {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));

        mockedSessionModule = new SessionModule() {
            @Override
            public SessionListContract.UserActionsListener provideSessionListPresenter(SessionService sessionService, PrefManager prefManager) {
                return new BaseMockSessionListPresenter(sessionService, prefManager) {
                    @Override
                    public void loadSessions() {
                        mSessionListView.setProgressIndicator(true);
                        List<SessionListItem> items = new ArrayList<>();

                        SessionListItem item1 = new SessionListItem();
                        item1.setSessionId(1);
                        item1.setSessionStatus(SessionStatus.CREATED);
                        SessionListItem item2 = new SessionListItem();
                        item1.setSessionId(2);
                        item1.setSessionStatus(SessionStatus.READY_TO_START);
                        SessionListItem item3 = new SessionListItem();
                        item1.setSessionId(3);
                        item1.setSessionStatus(SessionStatus.FINISHED);

                        items.add(item1);
                        items.add(item2);
                        items.add(item3);

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                        mSessionListView.setProgressIndicator(false);

                        mSessionListView.showSessions(items);
                    }
                };
            }
        };

        launchActivity();
        DashboardScreen screen = new DashboardScreen();

        Thread.sleep(500);

        screen.openSessions();

        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(
                        is(getTargetContext()
                                .getResources()
                                .getString(R.string.sessions_label)))));

        onView(withId(R.id.refresh_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.nothing_to_show_container)).check(matches(isDisplayed()));
        onView(withText(R.string.nothing_to_show_sessions)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignOut() throws Exception {
        // use the following 2 lines for saving a dummy access token.
        PrefManager prefManager = new PrefManager(PreferenceManager.getDefaultSharedPreferences(getTargetContext()));
        prefManager.saveAccessToken(new AccessToken("access-token", "token-type", "refresh-token", 1, new Date()));

        mockedDashboardModule = new DashboardModule() {
            @Override
            public DashboardContract.UserActionsListener provideDashboardPresenter(UserService userService, final PrefManager prefManager) {
                return new BaseMockDashboardPresenter(userService, prefManager, dummyUser) {
                    @Override
                    public void openOrganizations() {
                        mDashboardView.showOrganizations();
                    }

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

        Thread.sleep(500);

        screen.signOut();

        AssertionHelper.assertCurrentActivityIsInstanceOf(SignInActivity.class);
    }

    @Test
    public void testRedirectionToSignIn() throws Exception {
        launchActivity();

        Thread.sleep(500);

        RedirectTester.testUnauthenticatedRedirect();
    }

    @After
    public void tearDown() {
        PreferenceManager.getDefaultSharedPreferences(getTargetContext()).edit().clear().apply();
    }
}
