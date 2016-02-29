package be.kdg.teame.kandoe.core.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.util.preferences.Preferences;
import butterknife.Bind;

public abstract class BaseDrawerActivity extends BaseToolbarActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState != null)
            mFromSavedInstanceState = true;

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getToolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    prefManager.savePreference(Preferences.USER_LEARNED_DRAWER, true);
                }

                supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
            mDrawerLayout.openDrawer(GravityCompat.START);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
}
