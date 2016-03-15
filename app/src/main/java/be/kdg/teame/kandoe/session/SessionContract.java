package be.kdg.teame.kandoe.session;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;

public interface SessionContract {

    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);
        void showSession();
    }

    interface UserActionsListener extends InjectableUserActionsListener<View>, AuthenticatedContract.UserActionsListener {
    }
}
