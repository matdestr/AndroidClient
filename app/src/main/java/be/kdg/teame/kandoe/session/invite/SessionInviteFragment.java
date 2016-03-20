package be.kdg.teame.kandoe.session.invite;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.session.SessionActivity;
import be.kdg.teame.kandoe.session.XmlClickable;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionInviteFragment extends BaseFragment implements SessionInviteContract.View, XmlClickable {

    @Inject
    SessionInviteContract.UserActionsListener mSessionInvitePresenter;

    @Bind(R.id.form_email)
    TextView mEmailTextView;

    @Bind(R.id.input_container)
    ViewGroup mInputContainer;

    private ProgressDialog mProgressDialog;

    private int mSessionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionInvitePresenter.setView(this);

        mProgressDialog = DialogGenerator.createProgressDialog(getContext(), R.string.session_inviting_users);

        Bundle args = getArguments();
        mSessionId = args.getInt(SessionActivity.SESSION_ID, -1);

        changeToolbarTitle(R.string.session_invite_label);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_invite_users, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_session_invite_users, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_session_add_field:
                addInputField();
                break;
        }

        return super.onOptionsItemSelected(item);
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
    public void showInviteFailedError(String message) {
        DialogGenerator.showErrorDialog(getActivity(), message);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        DialogGenerator.showErrorDialog(getActivity(), R.string.error_connection_failure);
    }

    @OnClick(R.id.fab_continue)
    public void onInviteButtonClick() {
        List<String> emails = new ArrayList<>();

        for (int i = 0; i < mInputContainer.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) mInputContainer.getChildAt(i);

            if (viewGroup instanceof TextInputLayout) {
                View view = viewGroup.getChildAt(0);

                if (view instanceof EditText) {
                    String email = ((EditText) view).getText().toString();
                    emails.add(email);
                }
            }
        }

        if (emails.isEmpty())
            Log.d("emails", "null");

        mSessionInvitePresenter.inviteUsers(mSessionId, emails);
    }

    public void addInputField() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View formFieldView = inflater.inflate(R.layout.item_invite, null);

        mInputContainer.addView(formFieldView);
    }

    @Override
    public void onRemoveInviteClick(View view) {
        View obsoleteView = (View) view.getParent();
        ((ViewManager) obsoleteView.getParent()).removeView(obsoleteView);
    }
}
