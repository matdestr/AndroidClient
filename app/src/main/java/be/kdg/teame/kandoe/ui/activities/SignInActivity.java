package be.kdg.teame.kandoe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.kdg.teame.kandoe.R;
import butterknife.Bind;
import butterknife.ButterKnife;


public class SignInActivity extends BaseActivity {

    @Bind(R.id.btn_sign_in) Button mBtnSignIn;
    @Bind(R.id.link_sign_up) TextView mTextSignUpHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initialiseListeners();
    }

    private void initialiseListeners() {
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        mTextSignUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUp();
            }
        });
    }

    private void goToSignUp() {
        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(intent);
    }

    private void signIn() {
        //testing
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_signin;
    }
}
