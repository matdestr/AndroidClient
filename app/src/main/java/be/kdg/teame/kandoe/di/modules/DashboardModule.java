package be.kdg.teame.kandoe.di.modules;

import be.kdg.teame.kandoe.dashboard.DashboardContract;
import be.kdg.teame.kandoe.dashboard.DashboardPresenter;
import be.kdg.teame.kandoe.data.retrofit.services.UserService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import dagger.Module;
import dagger.Provides;

@Module
public class DashboardModule {
    @Provides
    public DashboardContract.UserActionsListener provideDashboardPresenter(UserService userService, PrefManager prefManager) {
        return new DashboardPresenter(userService, prefManager);
    }
}
