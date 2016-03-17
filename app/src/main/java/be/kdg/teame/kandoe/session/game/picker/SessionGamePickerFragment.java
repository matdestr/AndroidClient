package be.kdg.teame.kandoe.session.game.picker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionGamePickerFragment extends BaseFragment implements SessionGamePickerContract.View {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game_picker, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @OnClick(R.id.fab_next_card)
    public void onFabNextCardClick(){

    }

    @OnClick(R.id.fab_previous_card)
    public void onFabPreviousCardClick(){

    }

    @OnClick(R.id.fab_pick_card)
    public void onFabPickCardClick(){

    }

    @Override
    public void showNextCard(CardDetails cardDetails) {
        replaceData(cardDetails);
    }

    @Override
    public void showPreviousCard(CardDetails cardDetails) {
        replaceData(cardDetails);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }

    private void replaceData(CardDetails cardDetails){

    }
}
