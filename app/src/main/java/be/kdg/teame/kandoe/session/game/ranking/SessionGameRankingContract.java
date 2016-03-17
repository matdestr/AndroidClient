package be.kdg.teame.kandoe.session.game.ranking;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.session.game.DataListener;

public interface SessionGameRankingContract {
    interface View extends AuthenticatedContract.View, WebDataView {

    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener, DataListener {

    }
}
