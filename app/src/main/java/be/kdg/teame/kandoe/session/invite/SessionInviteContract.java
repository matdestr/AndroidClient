package be.kdg.teame.kandoe.session.invite;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

public interface SessionInviteContract {

    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        void showInviteFailedError(String message);
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
        void inviteUsers(int sessionId, List<String> emails);
    }
}
