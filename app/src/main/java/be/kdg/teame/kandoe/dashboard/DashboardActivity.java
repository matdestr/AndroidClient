package be.kdg.teame.kandoe.dashboard;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.activities.BaseDrawerActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseDrawerActivity {

    @Bind(R.id.drawer) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view_main) NavigationView mNavigationViewMain;
    @Bind(R.id.nav_view_footer) NavigationView mNavigationViewFooter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        InitializeEvents();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_dashboard;
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
