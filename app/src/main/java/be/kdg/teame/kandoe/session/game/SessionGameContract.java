package be.kdg.teame.kandoe.session.game;


import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

public interface SessionGameContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void showRanking();
        void showCardPicker();
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadCardPositions(int sessionId);
        void addDataListener(DataListener listener);
    }
}
