package be.kdg.teame.kandoe.session.join;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.session.SessionActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionJoinFragment extends BaseFragment implements SessionJoinContract.View {
    @Bind(R.id.session_load_users_container)
    RelativeLayout mUserLoadContainer;

    @Bind(R.id.session_accept_container)
    RelativeLayout mJoinContainer;

    @Bind(R.id.join_progressbar)
    ProgressBar mProgressBar;

    @Bind(R.id.join_progresstext)
    TextView mProgressTextView;

    @Bind(R.id.session_title)
    TextView mTitleTextView;

    @Bind(R.id.organization_title)
    TextView mOrganizationTextView;

    @Bind(R.id.topic_title)
    TextView mTopicTextView;

    @Bind(R.id.category_title)
    TextView mCategoryTextView;

    @Bind(R.id.session_participant_amount)
    TextView mSessionParticipantView;

    private ProgressDialog mProgressDialogJoining;


    @Inject
    SessionJoinContract.UserActionsListener mSessionJoinPresenter;

    private int mSessionId;
    private int mParticipantAmount;

    private String mCategory;
    private String mTopic;
    private String mOrganization;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionJoinPresenter.setView(this);

        Bundle arguments = getArguments();

        mSessionId = arguments.getInt(SessionActivity.SESSION_ID);
        mParticipantAmount = arguments.getInt(SessionActivity.SESSION_PARTICIPANT_AMOUNT);
        mCategory = arguments.getString(SessionActivity.SESSION_CATEGORY_TITLE);
        mTopic = arguments.getString(SessionActivity.SESSION_TOPIC_TITLE);
        mOrganization = arguments.getString(SessionActivity.SESSION_ORGANIZATION_TITLE);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_join, container, false);
        ButterKnife.bind(this, root);

        mTitleTextView.setText(String.format("Session %d", mSessionId));
        mTopicTextView.setText(mTopic);
        mOrganizationTextView.setText(mOrganization);
        mCategoryTextView.setText(mCategory);
        mSessionParticipantView.setText(String.format("%d", mParticipantAmount));
        mProgressDialogJoining = DialogGenerator.createProgressDialog(getContext(), R.string.session_joining_session);


        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active)
            mProgressDialogJoining.show();
        else
            mProgressDialogJoining.dismiss();
    }


    @Override
    public void close() {
        this.getActivity().finish();
    }

    @Override
    public void showJoined() {
        mJoinContainer.setVisibility(View.GONE);
        mUserLoadContainer.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);
        mProgressTextView.setText(R.string.session_waiting_for_users_to_join);
    }

    @Override
    public void showUserJoined() {
        //todo show snackbar
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        DialogGenerator.showErrorDialog(getContext(), errorMessage);
    }

    @OnClick(R.id.btn_join)
    public void onJoinClick() {
        mSessionJoinPresenter.join(mSessionId);
    }

    @OnClick(R.id.btn_decline)
    public void onDeclineClick() {
        mSessionJoinPresenter.decline(mSessionId);
    }
}
