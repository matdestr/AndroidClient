package be.kdg.teame.kandoe.session.game;

import android.os.Bundle;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.activities.BaseToolbarActivity;
import be.kdg.teame.kandoe.di.components.AppComponent;

public class SessionGameActivity extends BaseToolbarActivity implements SessionGameContract.View {
    @Inject
    SessionGameContract.UserActionsListener mSessionGamePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        // todo proper layout file
        return R.layout.activity_profile;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showRanking() {

    }

    @Override
    public void showCardPicker() {

    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }
}
