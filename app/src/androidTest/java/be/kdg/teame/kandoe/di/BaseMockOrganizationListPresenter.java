package be.kdg.teame.kandoe.di;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.dashboard.organizationlist.OrganizationListContract;
import be.kdg.teame.kandoe.dashboard.organizationlist.OrganizationListPresenter;
import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListContract;
import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListPresenter;
import be.kdg.teame.kandoe.data.retrofit.services.OrganizationService;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class BaseMockOrganizationListPresenter extends OrganizationListPresenter {
    public OrganizationListContract.View mOrganizationListView;

    public BaseMockOrganizationListPresenter(OrganizationService organizationService, PrefManager prefManager) {
        super(organizationService, prefManager);
    }

    @Override
    public void setView(@NonNull OrganizationListContract.View view) {
        mOrganizationListView = view;
    }
}
