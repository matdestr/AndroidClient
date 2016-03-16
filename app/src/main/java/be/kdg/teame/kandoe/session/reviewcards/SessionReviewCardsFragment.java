package be.kdg.teame.kandoe.session.reviewcards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import butterknife.ButterKnife;

public class SessionReviewCardsFragment extends BaseFragment implements SessionReviewCardsContract.View {
    @Inject
    SessionReviewCardsContract.UserActionsListener mAddCardsPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_review_cards, container, false);
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
