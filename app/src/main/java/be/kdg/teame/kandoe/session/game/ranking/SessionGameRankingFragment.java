package be.kdg.teame.kandoe.session.game.ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.models.sessions.Session;
import butterknife.ButterKnife;
import lombok.Getter;

public class SessionGameRankingFragment extends BaseFragment implements SessionGameRankingContract.View {
    @Getter
    private SessionGameRankingContract.UserActionsListener mGameRankingContractPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game_ranking, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }
}
