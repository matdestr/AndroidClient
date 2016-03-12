package be.kdg.teame.kandoe.di.session;

import android.support.annotation.NonNull;

import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListContract;
import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListPresenter;
import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class BaseMockSessionListPresenter extends SessionListPresenter {
    public SessionListContract.View mSessionListView;

    public BaseMockSessionListPresenter(SessionService sessionService, PrefManager prefManager) {
        super(sessionService, prefManager);
    }

    @Override
    public void setView(@NonNull SessionListContract.View view) {
        mSessionListView = view;
    }
}
