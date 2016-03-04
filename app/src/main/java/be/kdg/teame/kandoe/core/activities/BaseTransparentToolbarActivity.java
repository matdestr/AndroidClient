package be.kdg.teame.kandoe.core.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import be.kdg.teame.kandoe.R;
import butterknife.Bind;

public abstract class BaseTransparentToolbarActivity extends BaseToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, getStatusBarHeight(), 0, 0);
        getToolbar().setLayoutParams(params);

    }
}
