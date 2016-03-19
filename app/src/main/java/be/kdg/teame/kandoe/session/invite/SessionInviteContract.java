package be.kdg.teame.kandoe.session.invite;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

public interface SessionInviteContract {

    interface View extends AuthenticatedContract.View, WebDataView {
        void showUserInvited();
        void showInviteFailedError(String message);
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void inviteUser(int sessionId, String email);
    }
}
