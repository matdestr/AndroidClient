package be.kdg.teame.kandoe.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Arne on 04/02/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
    }

    protected abstract int getLayoutResource();

}
