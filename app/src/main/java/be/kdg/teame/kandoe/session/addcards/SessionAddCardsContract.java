package be.kdg.teame.kandoe.session.addcards;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;

/**
 * Contract for the SessionAddCards feature
 */

public interface SessionAddCardsContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void setRefreshingProgressIndicator(boolean active);

        void setProgressIndicator(boolean active);

        void showCards(List<CardDetails> cardDetails);

        void showWaitingForOtherParticipants();
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadCards(int sessionId);

        void addCard(int sessionId, CardDetails cardDetails);

        void finishedAddingCards(int sessionId);

        void checkIfUserCanAddCards(int sessionId);
    }
}
