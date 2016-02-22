package be.kdg.teame.kandoe.ui.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import be.kdg.teame.kandoe.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mathisse on 19/02/2016.
 */
public class SignUpActivity extends BaseActivity {

    @Bind(R.id.btn_sign_up)
    Button mBtnSignUp;
    @Bind(R.id.link_sign_in)
    TextView mTextSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setStatusBarColor();
        initialiseListeners();
    }

    private void initialiseListeners() {
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        mTextSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignIn();
            }
        });
    }

    private void goToSignIn() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    private void signUp() {
        //testing
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor() {
        Activity activity = SignUpActivity.this;
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_signup;
    }
}
