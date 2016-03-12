package be.kdg.teame.kandoe.profile.edit;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.users.dto.UpdateUserDTO;

public interface ProfileEditContract {

    interface View extends AuthenticatedContract.View, WebDataView {
        void showProfile();
    }

    interface UserActionsListener extends InjectableUserActionsListener<ProfileEditContract.View>, AuthenticatedContract.UserActionsListener {

        void updateUser(int userId, UpdateUserDTO updateUserDTO);
    }
}
