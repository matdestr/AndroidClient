package be.kdg.teame.kandoe.session.finish;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;

/**
<<<<<<< Updated upstream
 * Interface that defines a contract between {@link SessionFinishFragment} and {@link SessionFinishPresenter}.
 * This allows the view and the presenter to communicate with each other.
=======
>>>>>>> Stashed changes
 * Contract for the MVP structure of the Session Finish feature
 */

public interface SessionFinishContract {

    interface View extends AuthenticatedContract.View, WebDataView {

        void setCards(List<CardDetails> cardDetailses);
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {

        void getWinners(int sessionId);
    }
}
