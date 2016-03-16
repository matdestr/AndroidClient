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
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.session.join.SessionJoinFragment;

public class SessionActivity extends BaseToolbarActivity implements SessionContract.View {
    public static final String SESSION_ID = "SESSION_ID";
    public static final String SESSION_PARTICIPANT_AMOUNT = "SESSION_PARTICIPANT_AMOUNT";
    public static final String SESSION_CATEGORY_TITLE = "SESSION_CATEGORY_TITLE";
    public static final String SESSION_TOPIC_TITLE = "SESSION_TOPIC_TITLE";
    public static final String SESSION_ORGANIZATION_TITLE = "SESSION_ORGANIZATION_TITLE";


    @Inject
    SessionContract.UserActionsListener mSessionPresenter;

    private BaseFragment mCurrentFragment;

    private Session mCurrentSession;

    private String mCategoryTitle;
    private String mTopicTitle;
    private String mOrganizationTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent bundles = getIntent();
        int sessionId = bundles.getIntExtra(SESSION_ID, -1);
        mCategoryTitle = bundles.getStringExtra(SESSION_CATEGORY_TITLE);
        mTopicTitle = bundles.getStringExtra(SESSION_TOPIC_TITLE);
        mOrganizationTitle = bundles.getStringExtra(SESSION_ORGANIZATION_TITLE);

        mSessionPresenter.setView(this);
        mSessionPresenter.loadSession(sessionId);
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
        args.putInt(SESSION_ID, mCurrentSession.getSessionId());
        args.putInt(SESSION_PARTICIPANT_AMOUNT, mCurrentSession.getParticipantInfo().size());
        args.putString(SESSION_CATEGORY_TITLE, mCategoryTitle);
        args.putString(SESSION_TOPIC_TITLE, mTopicTitle);
        args.putString(SESSION_ORGANIZATION_TITLE, mOrganizationTitle);

        Log.d(getClass().getSimpleName(), String.format("switchFragment - switching to fragment %s mSessionId: %d", fragment.getClass().getSimpleName(), mCurrentSession.getSessionId()));

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
    public void showSession(Session session) {
        this.mCurrentSession = session;
        switchFragment(new SessionJoinFragment());
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }
}
