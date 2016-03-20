package be.kdg.teame.kandoe.dashboard.organizationlist;

import java.util.List;

import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.core.contracts.InjectableUserActionsListener;
import be.kdg.teame.kandoe.core.contracts.WebDataView;
import be.kdg.teame.kandoe.models.organizations.Organization;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;

/**
 * Interface that defines a contract between {@link OrganizationListContract} and {@link OrganizationListPresenter}.
 * This allows the view and the presenter to communicate with each other.
 * */
public interface OrganizationListContract {

    /**
     * Interface that defines the methods a View should implement.
     * @see AuthenticatedContract.View
     * @see WebDataView
     * */
    interface View extends AuthenticatedContract.View, WebDataView {
        /**
         * Shows or hides a dialog that indicates the progress of an action.
         * @param active boolean that indicates wheter the dialog should show or hide.
         * */
        void setProgressIndicator(boolean active);

        /**
         * Shows a list of {@link Organization} items to the user
         * @param organizations the list of organizations to show
         */
        void showOrganizations(List<Organization> organizations);

        /**
         * Shows an error message stating no sessions where found or hides this message and shows the sessions.
         * @param active boolean that indicates whether the error message should be shown or hidden
         */
        void showZeroOrganizationsFoundMessage(boolean active);
    }

    /**
     * Interface that defines the methods that can be fired because of user interactions.
     * @see InjectableUserActionsListener
     * @see AuthenticatedContract.UserActionsListener
     * */
    interface UserActionsListener extends InjectableUserActionsListener<OrganizationListContract.View>, AuthenticatedContract.UserActionsListener {
        /**
         * loads the organizations where the current user is a member of
         */
        void loadOrganizations();
    }
}
