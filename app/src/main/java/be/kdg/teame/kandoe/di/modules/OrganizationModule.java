package be.kdg.teame.kandoe.di.modules;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.dashboard.organizationlist.OrganizationListContract;
import be.kdg.teame.kandoe.dashboard.organizationlist.OrganizationListPresenter;
import be.kdg.teame.kandoe.data.retrofit.ServiceGenerator;
import be.kdg.teame.kandoe.data.retrofit.services.OrganizationService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import dagger.Module;
import dagger.Provides;

/**
 * Module that provides objects for injections for the Organization activities
 */


@Module
public class OrganizationModule {
    @Provides
    @Singleton
    public OrganizationService provideSessionService(PrefManager prefManager) {
        return ServiceGenerator.createAuthenticatedService(OrganizationService.class, prefManager);
    }


    @Provides
    public OrganizationListContract.UserActionsListener provideOrganizationListPresenter(OrganizationService organizationService, PrefManager prefManager) {
        return new OrganizationListPresenter(organizationService, prefManager);
    }
}
