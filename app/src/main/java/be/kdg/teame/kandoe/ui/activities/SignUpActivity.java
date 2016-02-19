package be.kdg.teame.kandoe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_signup;
    }
}
