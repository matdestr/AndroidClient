package be.kdg.teame.kandoe.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.activities.BaseToolbarActivity;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.session.join.SessionJoinFragment;

public class SessionActivity extends BaseToolbarActivity {

    public static final String SESSION_ID = "SESSION_ID";

    private BaseFragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent bundles = getIntent();
        int sessionId = bundles.getIntExtra(SESSION_ID, -1);

        switchFragment(new SessionJoinFragment());

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_session;
    }

    private void switchFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (mCurrentFragment == null)
            transaction.add(R.id.fragment_container, fragment);
        else
            transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();

        mCurrentFragment = fragment;
    }
}
