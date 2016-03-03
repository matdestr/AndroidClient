package be.kdg.teame.kandoe.di.modules;

import javax.inject.Singleton;

import be.kdg.teame.kandoe.dashboard.DashboardContract;
import be.kdg.teame.kandoe.dashboard.DashboardPresenter;
import be.kdg.teame.kandoe.data.retrofit.ServiceGenerator;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.profile.ProfileContract;
import be.kdg.teame.kandoe.profile.ProfilePresenter;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    @Provides
    @Singleton
    public UserService provideUserService(PrefManager prefManager) {
        return ServiceGenerator.createAuthenticatedService(UserService.class, prefManager);
    }

    @Provides
    public ProfileContract.UserActionsListener provideProfilePresenter(UserService userService, PrefManager prefManager) {
        return new ProfilePresenter(userService, prefManager);
    }

}
