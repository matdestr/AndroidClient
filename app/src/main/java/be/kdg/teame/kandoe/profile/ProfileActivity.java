package be.kdg.teame.kandoe.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseToolbarActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.profile.edit.ProfileEditActivity;
import be.kdg.teame.kandoe.views.reveal.RevealLayout;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * View (Activity) for the Profile feature
 */

public class ProfileActivity extends BaseToolbarActivity implements ProfileContract.View {

    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Bind(R.id.data_profile_pic)
    ImageView mImageViewDataPic;

    @Bind(R.id.data_profile_username)
    TextView mTextViewDataUsername;

    @Bind(R.id.data_profile_email)
    TextView mTextViewDataEmail;

    @Bind(R.id.data_profile_firstname)
    TextView mTextViewDataFirstname;

    @Bind(R.id.data_profile_lastname)
    TextView mTextViewDataLastname;

    @Bind(R.id.reveal_layout)
    RevealLayout mRevealLayout;

    @Bind(R.id.reveal_view)
    View mRevealView;

    @Bind(R.id.fab_edit)
    FloatingActionButton mFabEdit;

    @Inject
    ProfileContract.UserActionsListener mProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfilePresenter.setView(this);
        mCollapsingToolbarLayout.setTitleEnabled(false);
        getToolbar().setTitle(prefManager.retrieveUsername());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProfilePresenter.checkUserIsAuthenticated();
        mProfilePresenter.loadUserdata();

    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showRetrievingDataStatus() {
        setTextRetrievingData(R.string.retrieving_data);
    }

    @Override
    public void showUserdata(User user) {
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setTitle(user.getFirstName() + " " + user.getLastName());
        mTextViewDataUsername.setText(user.getUsername());
        mTextViewDataEmail.setText(user.getEmail());
        mTextViewDataFirstname.setText(user.getFirstName());
        mTextViewDataLastname.setText(user.getLastName());
        Picasso.with(this)
                .load(user.getProfilePictureUrl())
                .placeholder(R.drawable.default_user)
                .into(mImageViewDataPic);
    }

    @Override
    public void showEdit(User user) {
        mFabEdit.setClickable(false);
        int[] location = new int[2];
        mFabEdit.getLocationOnScreen(location);
        location[0] += mFabEdit.getWidth() / 2;
        location[1] += mFabEdit.getHeight() / 2;

        final Intent intent = new Intent(this, ProfileEditActivity.class);

        Gson gson = new Gson();
        String json = gson.toJson(user);

        intent.putExtra(ProfileEditActivity.USER, json);

        mRevealView.setVisibility(View.VISIBLE);
        mRevealLayout.setVisibility(View.VISIBLE);

        // Play reveal animation (expand from center of FAB)
        mRevealLayout.show(location[0], location[1]);
        mFabEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                // Preventing flashing screen on transition with anim hold.xml
                overridePendingTransition(0, R.anim.hold);
            }
        }, 600); // 600 is default duration of reveal animation in RevealLayout
        mFabEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFabEdit.setClickable(true);
                mRevealLayout.setVisibility(View.INVISIBLE);
            }
        }, 960); // Or some numbers larger than 600.
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        if (errorMessage != null)
            DialogGenerator.showErrorDialog(this, errorMessage);
        else
            DialogGenerator.showErrorDialog(this, R.string.error_connection_failure);
    }

    private void setTextRetrievingData(int stringId) {
        mTextViewDataUsername.setText(stringId);
        mTextViewDataEmail.setText(stringId);
        mTextViewDataFirstname.setText(stringId);
        mTextViewDataLastname.setText(stringId);
    }

    @OnClick(R.id.fab_edit)
    public void clickFabEdit() {
        mProfilePresenter.openEditMode();
    }

}
