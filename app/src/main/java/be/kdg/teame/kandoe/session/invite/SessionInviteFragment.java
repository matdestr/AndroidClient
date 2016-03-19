package be.kdg.teame.kandoe.session.invite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SessionInviteFragment extends BaseFragment implements SessionInviteContract.View {

    @Inject
    SessionInviteContract.UserActionsListener mSessionInvitePresenter;

    @Bind(R.id.form_email)
    TextView mEmailTextView;

    @Bind(R.id.session_load_container)
    RelativeLayout mSessionLoadContainer;

    @Bind(R.id.session_invite_container)
    RelativeLayout mSessionInviteContainer;

    private int mSessionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionInvitePresenter.setView(this);

        Bundle args = getArguments();
        mSessionId = args.getInt(SessionActivity.SESSION_ID, -1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_invite_users, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showUserInvited() {
        mSessionInviteContainer.setVisibility(View.GONE);
        mSessionLoadContainer.setVisibility(View.VISIBLE);
        Log.d(getClass().getSimpleName(), "Waiting for other users...");
    }

    @Override
    public void showInviteFailedError(String message) {
        DialogGenerator.showErrorDialog(getActivity(), message);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        DialogGenerator.showErrorDialog(getActivity(), R.string.error_connection_failure);
    }

    @OnClick(R.id.btn_invite)
    public void onInviteButtonClick(){
        String email = mEmailTextView.getText().toString();

        if (email.isEmpty())
            return;

        mSessionInvitePresenter.inviteUser(mSessionId, email);
    }
}
