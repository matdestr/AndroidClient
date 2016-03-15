package be.kdg.teame.kandoe.session;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class SessionPresenter implements SessionContract.UserActionsListener {
    private SessionContract.View mSessionView;

    @Inject
    public SessionPresenter(SessionService sessionService, PrefManager prefManager){
    }

    @Override
    public void setView(@NonNull SessionContract.View view) {
        mSessionView = view;
    }

    @Override
    public void checkUserIsAuthenticated() {

    }
}
