package be.kdg.teame.kandoe.session.reviewcards;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.sessions.dto.CreateReviewDTO;

public interface SessionReviewCardsContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        void showCards(List<CardDetails> cardDetails);

        void showWaitingForOtherParticipants();
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void loadCards(int sessionId);

        void saveReview(CardDetails cardDetails, String review);

        void postReviews(int sessionId);
    }
}
