package be.kdg.teame.kandoe.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseDrawerActivity;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.dashboard.organizationlist.OrganizationListFragment;
import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.profile.ProfileActivity;
import butterknife.Bind;
import butterknife.ButterKnife;


public class DashboardActivity extends BaseDrawerActivity implements DashboardContract.View {

    private BaseFragment mCurrentFragment;

    @Bind(R.id.drawer_nav_view)
    NavigationView mNavigationView;

    @Inject
    DashboardContract.UserActionsListener mDashboardPresenter;

    private ImageView mImageViewProfilePic;
    private TextView mTextViewProfileName;
    private TextView mTextViewProfileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
        mDashboardPresenter.setView(this);
        InitializeEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDashboardPresenter.checkUserIsAuthenticated();
        mDashboardPresenter.loadUserdata();
        mDashboardPresenter.openOrganizations();
    }

    private void initializeComponents() {
        android.view.View navheader = mNavigationView.getHeaderView(0);

        mImageViewProfilePic = ButterKnife.findById(navheader, R.id.navheader_profile_pic);
        mTextViewProfileName = ButterKnife.findById(navheader, R.id.navheader_profile_name);
        mTextViewProfileEmail = ButterKnife.findById(navheader, R.id.navheader_profile_email);
    }

    private void InitializeEvents() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_sign_out:
                        mDashboardPresenter.signOut();
                        break;

                    case R.id.action_my_organizations:
                        mDashboardPresenter.openOrganizations();
                        break;

                    case R.id.action_active_sessions:
                        mDashboardPresenter.openSessions();
                        break;

                    default:
                        Log.d("click-event", "main navigation view menu-item clicked");
                }

                menuItem.setChecked(true);

                getDrawerLayout().closeDrawers();

                return true;
            }
        });

        mImageViewProfilePic.setOnClickListener(onProfileClick());
        mTextViewProfileName.setOnClickListener(onProfileClick());
        mTextViewProfileEmail.setOnClickListener(onProfileClick());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getDrawerLayout().openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showUserdata(User user) {
        mTextViewProfileName.setText(user.getUsername());
        mTextViewProfileEmail.setText(user.getEmail());
        Picasso.with(this)
                .load(user.getProfilePictureUrl())
                .placeholder(R.drawable.default_user)
                .into(mImageViewProfilePic);
    }

    @Override
    public void showSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent, true);
    }

    @Override
    public void showProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void showOrganizations() {
        getToolbar().setTitle(R.string.organizations_label);
        switchFragment(new OrganizationListFragment());
    }

    @Override
    public void showSessions() {
        getToolbar().setTitle(R.string.sessions_label);
        switchFragment(new SessionListFragment());
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        if (errorMessage != null)
            DialogGenerator.showErrorDialog(this, errorMessage);
        else
            DialogGenerator.showErrorDialog(this, R.string.error_connection_failure);
    }

    private View.OnClickListener onProfileClick() {
        View.OnClickListener profileClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDashboardPresenter.openProfile();
            }
        };

        return profileClickListener;
    }

    private void switchFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (mCurrentFragment == null)
            transaction.add(R.id.fragment_container, fragment);
        else
            transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();

        mCurrentFragment = fragment;
    }

}
