package be.kdg.teame.kandoe.dashboard.organizationlist;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.organizations.Organization;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;

public interface OrganizationListContract {
    interface View extends AuthenticatedContract.View, WebDataView {
        void setProgressIndicator(boolean active);

        void showOrganizations(List<Organization> organizations);

        void showZeroOrganizationsFoundMessage(boolean active);
    }

    interface UserActionsListener extends InjectableUserActionsListener<OrganizationListContract.View>, AuthenticatedContract.UserActionsListener {
        void loadOrganizations();
    }
}
