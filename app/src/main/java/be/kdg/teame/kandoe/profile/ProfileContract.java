package be.kdg.teame.kandoe.profile;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.users.User;

public interface ProfileContract {

    interface View extends AuthenticatedContract.View, WebDataView {
        void showRetrievingDataStatus();

        void showUserdata(User user);

        void showEdit(User user);

    }

    interface UserActionsListener extends InjectableUserActionsListener<ProfileContract.View>, AuthenticatedContract.UserActionsListener {
        void loadUserdata();

        void openEditMode();
    }
}
