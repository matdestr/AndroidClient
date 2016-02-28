package be.kdg.teame.kandoe.core.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import be.kdg.teame.kandoe.R;
import butterknife.Bind;

public abstract class BaseToolbarActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
