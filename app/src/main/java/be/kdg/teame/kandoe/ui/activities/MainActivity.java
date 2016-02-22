package be.kdg.teame.kandoe.ui.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import be.kdg.teame.kandoe.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view_main) NavigationView mNavigationViewMain;
    @Bind(R.id.nav_view_footer) NavigationView mNavigationViewFooter;
    @Bind(R.id.toolbar) Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        ConfigureToolbar();

        InitializeEvents();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void ConfigureToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void InitializeEvents() {
        mNavigationViewMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    default:
                        Log.d("click-event", "main navigation view menu-item clicked");
                }

                menuItem.setChecked(true);

                mDrawerLayout.closeDrawers();

                return true;
            }
        });

        mNavigationViewFooter.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_settings:
                        Log.d("click-event", "settings clicked");
                        break;
                    case R.id.action_sign_out:
                        Log.d("click-event", "settings sign out");
                        break;
                }

                menuItem.setChecked(true);

                mDrawerLayout.closeDrawers();

                return true;
            }
        });
    }
}
