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
import be.kdg.teame.kandoe.models.sessions.SessionStatus;
import be.kdg.teame.kandoe.session.addcards.SessionAddCardsFragment;
import be.kdg.teame.kandoe.session.choosecards.SessionChooseCardsFragment;
import be.kdg.teame.kandoe.session.game.SessionGameFragment;
import be.kdg.teame.kandoe.session.join.SessionJoinFragment;
import be.kdg.teame.kandoe.session.reviewcards.SessionReviewCardsFragment;

public class SessionActivity extends BaseToolbarActivity implements SessionContract.View {
    public static final String SESSION_ID = "SESSION_ID";
    public static final String SESSION_PARTICIPANT_AMOUNT = "SESSION_PARTICIPANT_AMOUNT";
    public static final String SESSION_CATEGORY_TITLE = "SESSION_CATEGORY_TITLE";
    public static final String SESSION_TOPIC_TITLE = "SESSION_TOPIC_TITLE";
    public static final String SESSION_ORGANIZATION_TITLE = "SESSION_ORGANIZATION_TITLE";
    public static final String SESSION_PARTICIPANT_CAN_ADD_CARDS = "SESSION_ORGANIZATION_TITLE";


    @Inject
    SessionContract.UserActionsListener mSessionPresenter;

    private BaseFragment mCurrentFragment;

    private Session mCurrentSession;

    private int mSessionId;
    private String mCategoryTitle;
    private String mTopicTitle;
    private String mOrganizationTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent bundles = getIntent();

        mSessionId = bundles.getIntExtra(SESSION_ID, -1);
        mCategoryTitle = bundles.getStringExtra(SESSION_CATEGORY_TITLE);
        mTopicTitle = bundles.getStringExtra(SESSION_TOPIC_TITLE);
        mOrganizationTitle = bundles.getStringExtra(SESSION_ORGANIZATION_TITLE);

        mSessionPresenter.setView(this);
        mSessionPresenter.loadSession(mSessionId);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_session;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSessionPresenter.openStatusListener(mSessionId);
    }

    private void switchFragment(BaseFragment fragment) {
        if (fragment == null) {
            Log.e("switch-fragment", "fragment cannot be null");
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Log.d(getClass().getSimpleName(),
                String.format("switchFragment - switching to fragment %s mSessionId: %d",
                        fragment.getClass().getSimpleName(), mCurrentSession.getSessionId())
        );

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

        BaseFragment fragment = chooseFragment(mCurrentSession.getSessionStatus());

        if (fragment != null)
            switchFragment(fragment);
    }

    private BaseFragment chooseFragment(SessionStatus sessionStatus) {
        BaseFragment fragment = null;

        Bundle args = new Bundle();
        args.putInt(SESSION_ID, mCurrentSession.getSessionId());

        switch (sessionStatus) {
            case CREATED:
                break;
            case USERS_JOINING:
                fragment = new SessionJoinFragment();
                args.putString(SESSION_CATEGORY_TITLE, mCategoryTitle);
                args.putString(SESSION_TOPIC_TITLE, mTopicTitle);
                args.putString(SESSION_ORGANIZATION_TITLE, mOrganizationTitle);
                args.putInt(SESSION_PARTICIPANT_AMOUNT, mCurrentSession.getParticipantInfo().size());
                break;
            case ADDING_CARDS:
                fragment = new SessionAddCardsFragment();
                args.putBoolean(SESSION_PARTICIPANT_CAN_ADD_CARDS, mCurrentSession.isParticipantsCanAddCards());
                break;
            case REVIEWING_CARDS:
                fragment = new SessionReviewCardsFragment();
                args.putBoolean(SESSION_PARTICIPANT_CAN_ADD_CARDS, mCurrentSession.isCardCommentsAllowed());
                break;
            case CHOOSING_CARDS:
                fragment = new SessionChooseCardsFragment();
                break;
            case READY_TO_START:
                //todo gamelauncher
                break;
            case IN_PROGRESS:
                fragment = new SessionGameFragment();
                break;
            case FINISHED:
                // todo finish
                mSessionPresenter.closeStatusListener();
                break;
        }

        if (fragment != null)
            fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onSessionStatusChanged(SessionStatus sessionStatus) {
        Log.d(getClass().getSimpleName(), "Session status changed to " + sessionStatus);
        switchFragment(chooseFragment(sessionStatus));
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }
}
