package be.kdg.teame.kandoe.core.activities;

import android.os.Bundle;

public abstract class BaseDrawerActivity extends BaseToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
