package be.kdg.teame.kandoe.session.game.picker;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;

public interface SessionGamePickerContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void showNextCard(CardDetails cardDetails);

        void showPreviousCard(CardDetails cardDetails);
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadNextCard();

        void loadPreviousCard();

        void pickCard();
    }
}
