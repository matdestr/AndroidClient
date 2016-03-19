package be.kdg.teame.kandoe.session.game.picker;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.session.game.DataListener;

public interface SessionGamePickerContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        void showNextCard(CardDetails cardDetails);

        void showPreviousCard(CardDetails cardDetails);

        void showCard(CardDetails cardDetails);
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener, DataListener {
        void loadNextCard();

        void loadPreviousCard();

        void pickCard(int sessionId);
    }
}
