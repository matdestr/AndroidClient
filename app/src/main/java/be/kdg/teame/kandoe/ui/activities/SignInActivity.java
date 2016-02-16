package be.kdg.teame.kandoe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import be.kdg.teame.kandoe.R;
import butterknife.Bind;
import butterknife.ButterKnife;


public class SignInActivity extends BaseActivity {

    @Bind(R.id.btn_sign_in)
    Button mBtnSignIn;

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
                signin();
            }
        });
    }

    private void signin() {
        //testing
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_signin;
    }
}
