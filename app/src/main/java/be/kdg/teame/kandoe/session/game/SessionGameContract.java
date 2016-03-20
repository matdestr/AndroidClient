package be.kdg.teame.kandoe.session.game;


import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardPosition;

public interface SessionGameContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void showRanking();
        void showCardPicker();
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadCardPositions(int sessionId);

        void openCurrentParticipantListener(int sessionId);

        void closeCurrentParticipantListener();

        void openCardPositionListener(int sessionId);

        void closeOpenCardPositionListener();

        void addDataListener(DataListener listener);
    }
}
