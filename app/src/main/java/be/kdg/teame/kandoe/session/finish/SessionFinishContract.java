package be.kdg.teame.kandoe.session.finish;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;

public interface SessionFinishContract {

    interface View extends AuthenticatedContract.View, WebDataView {

        void setCards(List<CardDetails> cardDetailses);
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {

        void getWinners(int sessionId);
    }
}
