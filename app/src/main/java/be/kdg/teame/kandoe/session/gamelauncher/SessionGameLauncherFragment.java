package be.kdg.teame.kandoe.session.gamelauncher;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.session.SessionActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Presenter for the Game Launcher feature
 */

public class SessionGameLauncherFragment extends BaseFragment implements SessionGameLauncherContract.View {

    @Bind(R.id.session_accept_container)
    RelativeLayout mAcceptContainer;

    @Bind(R.id.session_wait_container)
    RelativeLayout mWaitContainer;

    @Bind(R.id.wait_progressbar)
    ProgressBar mWaitProgressBar;

    @Inject
    SessionGameLauncherContract.UserActionsListener mSessionGameLauncherPresenter;


    private ProgressDialog mProgressDialog;
    private int mSessionId;
    private boolean mIsOrganizer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionGameLauncherPresenter.setView(this);

        Bundle arguments = getArguments();

        mSessionId = arguments.getInt(SessionActivity.SESSION_ID);
        mIsOrganizer = arguments.getBoolean(SessionActivity.SESSION_IS_ORGANIZER, false);

        changeToolbarTitle(R.string.session_game_launcher_label);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game_launcher, container, false);
        ButterKnife.bind(this, root);

        mProgressDialog = DialogGenerator.createProgressDialog(getContext(), R.string.session_starting_game);

        showWaitingForOtherParticipants(mIsOrganizer);

        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active)
            mProgressDialog.show();
        else
            mProgressDialog.dismiss();
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        DialogGenerator.showErrorDialog(getContext(), errorMessage);
    }

    public void showWaitingForOtherParticipants(boolean isOrganizer) {
        if (isOrganizer) return;

        mAcceptContainer.setVisibility(View.GONE);
        mWaitProgressBar.setIndeterminate(true);
        mWaitContainer.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_start_game)
    public void onStartGameClick() {
        mSessionGameLauncherPresenter.launchGame(mSessionId);
    }
}
