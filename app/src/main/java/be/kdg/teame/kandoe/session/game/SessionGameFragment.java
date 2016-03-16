package be.kdg.teame.kandoe.session.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.activities.BaseToolbarActivity;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import butterknife.ButterKnife;

public class SessionGameFragment extends BaseFragment implements SessionGameContract.View {
    @Inject
    SessionGameContract.UserActionsListener mSessionGamePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game, container, false);
        ButterKnife.bind(this, root);
        return root;
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
