package be.kdg.teame.kandoe.session.choosecards;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;

public interface SessionChooseCardsContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        void setRefreshingProgressIndicator(boolean active);

        void showCards(List<CardDetails> cardDetails);

        void showWaitingForOtherParticipants();

        void showNoCardsSelected();
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadCards(int sessionId);

        void chooseCards(int sessionId, List<Integer> cardIds);
    }
}
