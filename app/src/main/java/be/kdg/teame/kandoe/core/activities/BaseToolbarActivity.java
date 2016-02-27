package be.kdg.teame.kandoe.core.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import be.kdg.teame.kandoe.R;

public abstract class BaseToolbarActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
