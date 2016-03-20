package be.kdg.teame.kandoe.core.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import be.kdg.teame.kandoe.R;
import butterknife.Bind;

/**
 * Contains basic configuration for an activity view which contains a {@link Toolbar}.
 *
 * @see BaseActivity
 */
public abstract class BaseToolbarActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
