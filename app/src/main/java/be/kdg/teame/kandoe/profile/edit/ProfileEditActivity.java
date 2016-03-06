package be.kdg.teame.kandoe.profile.edit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.google.gson.Gson;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseTransparentToolbarActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.dto.CreateUserDTO;
import be.kdg.teame.kandoe.models.dto.UpdateUserDTO;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.validators.forms.Form;
import be.kdg.teame.kandoe.util.validators.forms.FormField;
import butterknife.Bind;
import butterknife.OnClick;

public class ProfileEditActivity extends BaseTransparentToolbarActivity implements ProfileEditContract.View {
    private Transition.TransitionListener mEnterTransitionListener;
    private User mUser;
    private Form mFrom;

    @Bind(R.id.form_username)
    EditText mEditTextFormUsername;

    @Bind(R.id.form_email)
    EditText mEditTextFormEmail;

    @Bind(R.id.form_first_name)
    EditText mEditTextFormFirstname;

    @Bind(R.id.form_last_name)
    EditText mEditTextFormLastname;

    @Bind(R.id.form_verifypassword)
    EditText mEditTextFormVerifyPassword;

    @Bind(R.id.fab_save)
    FloatingActionButton mFabSave;

    @Inject
    ProfileEditContract.UserActionsListener mProfileEditPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().setNavigationIcon(R.drawable.ic_action_remove);

        Bundle extras = getIntent().getExtras();

        Gson gson = new Gson();
        String json = extras.getString("user", null);

        mUser = gson.fromJson(json, User.class);

        mEditTextFormUsername.setText(mUser.getUsername());
        mEditTextFormEmail.setText(mUser.getEmail());
        mEditTextFormFirstname.setText(mUser.getFirstName());
        mEditTextFormLastname.setText(mUser.getLastName());

        configureTransition();

        mProfileEditPresenter.setView(this);

        mFrom = new Form();

        TextInputLayout tilUsername = (TextInputLayout) mEditTextFormUsername.getParent();
        TextInputLayout tilFirstName = (TextInputLayout) mEditTextFormFirstname.getParent();
        TextInputLayout tilLastName = (TextInputLayout) mEditTextFormLastname.getParent();
        TextInputLayout tilEmail = (TextInputLayout) mEditTextFormEmail.getParent();
        TextInputLayout tilVerifyPassword = (TextInputLayout) mEditTextFormVerifyPassword.getParent();

        mFrom.add(new FormField(tilUsername, mEditTextFormUsername, FormField.Type.USER_NAME, true));
        mFrom.add(new FormField(tilFirstName, mEditTextFormFirstname, true));
        mFrom.add(new FormField(tilLastName, mEditTextFormLastname, true));
        mFrom.add(new FormField(tilEmail, mEditTextFormEmail, FormField.Type.EMAIL, true));
        mFrom.add(new FormField(tilVerifyPassword, mEditTextFormVerifyPassword, FormField.Type.PASSWORD, true));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile_edit;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProfileEditPresenter.checkUserIsAuthenticated();
    }

    @Override
    public void onBackPressed() {
        exitReveal(mFabSave);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exitReveal(mFabSave);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        if (errorMessage != null)
            DialogGenerator.showErrorDialog(this, errorMessage);
        else
            DialogGenerator.showErrorDialog(this, R.string.error_connection_failure);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void configureTransition() {
        mEnterTransitionListener = new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                enterReveal(mFabSave);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        };
        getWindow().getEnterTransition().addListener(mEnterTransitionListener);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void enterReveal(final View view) {
        // get the center for the clipping circle
        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        //update remove listener
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
                getWindow().getEnterTransition().removeListener(mEnterTransitionListener);
            }
        });

        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitReveal(final View view) {
        // get the center for the clipping circle
        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = view.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
                supportFinishAfterTransition();
            }
        });

        // start the animation
        anim.start();
    }

    @Override
    public void showProfile() {
        finish();
    }

    @OnClick(R.id.fab_save)
    public void save() {
        if (mFrom.validate()) {
            UpdateUserDTO updateUserDTO = new UpdateUserDTO(
                    mEditTextFormUsername.getText().toString(),
                    mEditTextFormFirstname.getText().toString(),
                    mEditTextFormLastname.getText().toString(),
                    mEditTextFormEmail.getText().toString(),
                    mEditTextFormVerifyPassword.getText().toString()
            );

            mProfileEditPresenter.updateUser(mUser.getUserId(), updateUserDTO);
        }
    }
}
