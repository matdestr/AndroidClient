package be.kdg.teame.kandoe.dashboard;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.users.User;

public interface DashboardContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void showUserdata(User user);

        void showSignIn();

        void showProfile();

        void showOrganizations();

        void showSessions();
    }

    interface UserActionsListener extends InjectableUserActionsListener<DashboardContract.View>, AuthenticatedContract.UserActionsListener {
        void loadUserdata();

        void openProfile();

        void openOrganizations();

        void openSessions();

        void signOut();

    }
}
