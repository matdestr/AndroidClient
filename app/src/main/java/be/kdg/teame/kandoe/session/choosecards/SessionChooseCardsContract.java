package be.kdg.teame.kandoe.session.choosecards;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

public interface SessionChooseCardsContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void showCards();
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadCards(int sessionId);
    }
}
