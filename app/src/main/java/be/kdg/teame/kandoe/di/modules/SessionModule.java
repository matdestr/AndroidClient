package be.kdg.teame.kandoe.di.modules;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListContract;
import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListPresenter;
import be.kdg.teame.kandoe.data.retrofit.ServiceGenerator;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.session.SessionContract;
import be.kdg.teame.kandoe.session.SessionPresenter;
import be.kdg.teame.kandoe.session.addcards.SessionAddCardsContract;
import be.kdg.teame.kandoe.session.addcards.SessionAddCardsPresenter;
import be.kdg.teame.kandoe.session.choosecards.SessionChooseCardsContract;
import be.kdg.teame.kandoe.session.choosecards.SessionChooseCardsPresenter;
import be.kdg.teame.kandoe.session.game.SessionGameContract;
import be.kdg.teame.kandoe.session.game.SessionGamePresenter;
import be.kdg.teame.kandoe.session.game.picker.SessionGamePickerContract;
import be.kdg.teame.kandoe.session.game.picker.SessionGamePickerPresenter;
import be.kdg.teame.kandoe.session.game.ranking.SessionGameRankingContract;
import be.kdg.teame.kandoe.session.game.ranking.SessionGameRankingPresenter;
import be.kdg.teame.kandoe.session.invite.SessionInviteContract;
import be.kdg.teame.kandoe.session.invite.SessionInvitePresenter;
import be.kdg.teame.kandoe.session.join.SessionJoinContract;
import be.kdg.teame.kandoe.session.join.SessionJoinPresenter;
import be.kdg.teame.kandoe.session.reviewcards.SessionReviewCardsContract;
import be.kdg.teame.kandoe.session.reviewcards.SessionReviewCardsPresenter;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import dagger.Module;
import dagger.Provides;

@Module
public class SessionModule {
    @Provides
    @Singleton
    public SessionService provideSessionService(PrefManager prefManager) {
        return ServiceGenerator.createAuthenticatedService(SessionService.class, prefManager);
    }

    @Provides
    public SessionContract.UserActionsListener provideSessionPresenter(SessionService sessionService, PrefManager prefManager) {
        return new SessionPresenter(sessionService, prefManager);
    }

    @Provides
    public SessionListContract.UserActionsListener provideSessionListPresenter(SessionService sessionService, PrefManager prefManager){
        return new SessionListPresenter(sessionService, prefManager);
    }

    @Provides
    public SessionJoinContract.UserActionsListener provideSessionJoinPresenter(SessionService sessionService, PrefManager prefManager){
        return new SessionJoinPresenter(sessionService, prefManager);
    }

    @Provides
    public SessionAddCardsContract.UserActionsListener provideSessionAddCardsPresenter(SessionService sessionService, PrefManager prefManager){
        return new SessionAddCardsPresenter(sessionService, prefManager);
    }

    @Provides
    public SessionReviewCardsContract.UserActionsListener provideSessionReviewCardsPresenter(SessionService sessionService, PrefManager prefManager){
        return new SessionReviewCardsPresenter(sessionService, prefManager);
    }

    @Provides
    public SessionChooseCardsContract.UserActionsListener provideSessionChooseCardsPresenter(SessionService sessionService, PrefManager prefManager){
        return new SessionChooseCardsPresenter(sessionService, prefManager);
    }

    @Provides
    public SessionGameContract.UserActionsListener provideSessionGamePresenter(SessionService sessionService, PrefManager prefManager) {
        return new SessionGamePresenter(sessionService, prefManager);
    }

    @Provides
    public SessionGamePickerContract.UserActionsListener provideSessionGamePickerPresenter(PrefManager prefManager) {
        return new SessionGamePickerPresenter(prefManager);
    }

    @Provides
    public SessionGameRankingContract.UserActionsListener provideSessionGameRankingPresenter(PrefManager prefManager) {
        return new SessionGameRankingPresenter(prefManager);
    }

    @Provides
    public SessionInviteContract.UserActionsListener provideSessionInvitePresenter(SessionService sessionService, PrefManager prefManager){
        return new SessionInvitePresenter(sessionService, prefManager);
    }
}
