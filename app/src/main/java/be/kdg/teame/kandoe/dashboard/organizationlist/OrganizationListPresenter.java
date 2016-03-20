package be.kdg.teame.kandoe.dashboard.organizationlist;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import be.kdg.teame.kandoe.core.AuthenticationHelper;
import be.kdg.teame.kandoe.data.retrofit.services.OrganizationService;
import be.kdg.teame.kandoe.models.organizations.Organization;
import be.kdg.teame.kandoe.util.http.ErrorResponse;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import lombok.val;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrganizationListPresenter implements OrganizationListContract.UserActionsListener {
    private OrganizationListContract.View mOrganizationView;
    private final OrganizationService mOrganizationService;
    private final PrefManager mPrefManager;

    public OrganizationListPresenter(OrganizationService organizationService, PrefManager prefManager) {
        this.mOrganizationService = organizationService;
        this.mPrefManager = prefManager;
    }

    @Override
    public void loadOrganizations() {
        mOrganizationView.showZeroOrganizationsFoundMessage(false);
        mOrganizationView.setProgressIndicator(true);

        String username = mPrefManager.retrieveUsername();
        if (username != null) {
            mOrganizationService.getOrganizations(username, true, new Callback<List<Organization>>() {
                @Override
                public void success(List<Organization> organizations, Response response) {
                    mOrganizationView.setProgressIndicator(false);

                    if (organizations.size() > 0)
                        mOrganizationView.showOrganizations(organizations);
                    else
                        mOrganizationView.showZeroOrganizationsFoundMessage(true);
                }

                @Override
                public void failure(RetrofitError error) {
                    mOrganizationView.setProgressIndicator(false);

                    if (error != null && error.getResponse() != null) {
                        val response = error.getResponse();

                        switch (response.getStatus()) {
                            case HttpStatus.BAD_REQUEST:
                                ErrorResponse.FieldError fieldError = (ErrorResponse.FieldError) error.getBodyAs(ErrorResponse.FieldError.class);
                                mOrganizationView.showErrorConnectionFailure(fieldError.getMessage());
                                break;
                            default:
                                mOrganizationView.showErrorConnectionFailure("Unable to load your organizations, please try again later.");

                        }
                    } else {
                        mOrganizationView.showErrorConnectionFailure(null);
                    }
                }
            });
        } else {
            Log.w(getClass().getSimpleName(), "Couldn't load organizations because username is null");
        }
    }

    @Override
    public void setView(@NonNull OrganizationListContract.View view) {
        mOrganizationView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {
        AuthenticationHelper.checkUserIsAuthenticated(mPrefManager, mOrganizationView);
    }
}
