package be.kdg.teame.kandoe.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.activities.BaseToolbarActivity;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.session.join.SessionJoinFragment;

public class SessionActivity extends BaseToolbarActivity implements SessionContract.View {
    public static final String SESSION_ID = "SESSION_ID";

    @Inject
    SessionContract.UserActionsListener mSessionPresenter;

    private BaseFragment mCurrentFragment;

    private int sessionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent bundles = getIntent();
        sessionId = bundles.getIntExtra(SESSION_ID, -1);

        mSessionPresenter.setView(this);
        switchFragment(new SessionJoinFragment());

    }

    @Override
    protected void onResume() {
        Intent bundles = getIntent();
        sessionId = bundles.getIntExtra(SESSION_ID, -1);
        Log.d(getClass().getSimpleName(), "onResume - Current sessionId: " + sessionId);

        super.onResume();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_session;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    private void switchFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Bundle args = new Bundle();
        args.putInt(SESSION_ID, sessionId);

        Log.d(getClass().getSimpleName(), "switchFragment - sessionId: " + sessionId);

        fragment.setArguments(args);

        if (mCurrentFragment == null)
            transaction.add(R.id.fragment_container, fragment);
        else
            transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();

        mCurrentFragment = fragment;
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    @Override
    public void showSession() {

    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }
}
