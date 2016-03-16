package be.kdg.teame.kandoe.session.choosecards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.session.addcards.SessionAddCardsContract;
import butterknife.ButterKnife;

public class SessionChooseCardsFragment extends BaseFragment implements SessionAddCardsContract.View  {
    @Inject
    SessionAddCardsContract.UserActionsListener mAddCardsPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_choose_cards, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }
}
