package be.kdg.teame.kandoe.session.choosecards;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;

/**
 * Interface that defines a contract between {@link SessionChooseCardsFragment} and {@link SessionChooseCardsPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface SessionChooseCardsContract {

    /**
     * Interface that defines the methods a View should implement
     * @see WebDataView
     * */
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        void setRefreshingProgressIndicator(boolean active);

        void showCards(List<CardDetails> cardDetails);

        void showWaitingForOtherParticipants();

        void showNoCardsSelected();
    }

    /**
     * Interface that defines the methods that can be fired because of user interactions
     * @see InjectableUserActionsListener
     * */
    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadCards(int sessionId);

        void chooseCards(int sessionId, List<Integer> cardIds);
    }
}
