package be.kdg.teame.kandoe.session.join;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.session.SessionActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionJoinFragment extends BaseFragment implements SessionJoinContract.View {

    @Inject
    SessionJoinContract.UserActionsListener mSessionJoinPresenter;

    private int sessionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionJoinPresenter.setView(this);
    }

    @Override
    public void onResume() {
        sessionId = getArguments().getInt(SessionActivity.SESSION_ID);
        super.onResume();
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
        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    @Override
    public void showError(String error) {
        DialogGenerator.showErrorDialog(getActivity(), error);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
            DialogGenerator.showErrorDialog(getContext(), errorMessage);
    }

    @OnClick(R.id.btn_join)
    public void onJoinClick(){
        mSessionJoinPresenter.join(sessionId);
    }

    @OnClick(R.id.btn_decline)
    public void onDeclineClick() { mSessionJoinPresenter.decline(sessionId); }
}
