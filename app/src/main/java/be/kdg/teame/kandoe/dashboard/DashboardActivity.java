package be.kdg.teame.kandoe.dashboard;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.MenuItem;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.activities.BaseDrawerActivity;
import butterknife.Bind;

public class DashboardActivity extends BaseDrawerActivity {

    @Bind(R.id.drawer_nav_view) NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                getDrawerLayout().openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void InitializeEvents() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    default:
                        Log.d("click-event", "main navigation view menu-item clicked");
                }

                menuItem.setChecked(true);

                getDrawerLayout().closeDrawers();

                return true;
            }
        });
    }
}
