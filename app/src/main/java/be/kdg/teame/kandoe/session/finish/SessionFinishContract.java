package be.kdg.teame.kandoe.session.finish;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;

/**
 * Interface that defines a contract between {@link SessionFinishFragment} and {@link SessionFinishPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface SessionFinishContract {

    /**
     * Interface that defines the methods a View should implement
     * @see WebDataView
     * */
    interface View extends AuthenticatedContract.View, WebDataView {

        void setCards(List<CardDetails> cardDetailses);
    }

    /**
     * Interface that defines the methods that can be fired because of user interactions
     * @see InjectableUserActionsListener
     * */
    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {

        void getWinners(int sessionId);
    }
}
