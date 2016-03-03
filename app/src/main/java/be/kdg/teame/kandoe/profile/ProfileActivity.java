package be.kdg.teame.kandoe.profile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.activities.BaseToolbarActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.users.User;
import butterknife.Bind;

public class ProfileActivity extends BaseToolbarActivity implements ProfileContract.View {

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

    @Inject
    ProfileContract.UserActionsListener mProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfilePresenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProfilePresenter.checkUserIsAuthenticated();
        mProfilePresenter.retrieveUserdata();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
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
    public void loadUserData(User user) {
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
    public void showErrorConnectionFailure() {

    }

    private void setTextRetrievingData(int stringId) {
        mTextViewDataUsername.setText(stringId);
        mTextViewDataEmail.setText(stringId);
        mTextViewDataFirstname.setText(stringId);
        mTextViewDataLastname.setText(stringId);
    }

}
