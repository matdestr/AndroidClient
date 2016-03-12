package be.kdg.teame.kandoe.dashboard.sessionlist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import be.kdg.teame.kandoe.data.retrofit.services.SessionService;
import be.kdg.teame.kandoe.models.sessions.Session;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;

@RunWith(MockitoJUnitRunner.class)
public class SessionListPresenterTest {
    @Mock
    private SessionListContract.View mSessionListView;

    private SessionListContract.UserActionsListener mSessionListPresenter;

    @Mock
    private SessionService mSessionService;

    @Mock
    private PrefManager mPrefManager;

    @Captor
    private ArgumentCaptor<Callback<List<SessionListItem>>> mSessionCallbackCaptor;

    @Before
    public void setUp() {
        mSessionListPresenter = new SessionListPresenter(mSessionService, mPrefManager);
        mSessionListPresenter.setView(mSessionListView);
    }

    @Test
    public void testLoadSessions() {
        mSessionListPresenter.loadSessions();

        Mockito.verify(mSessionListView).showZeroSessionsFoundMessage(Mockito.eq(false));
        Mockito.verify(mSessionListView).setProgressIndicator(Mockito.eq(true));
        Mockito.verify(mSessionService).getSessions(mSessionCallbackCaptor.capture());

        List<SessionListItem> sessions = new ArrayList<>();
        sessions.add(new SessionListItem());

        mSessionCallbackCaptor.getValue().success(sessions, null);

        Mockito.verify(mSessionListView).setProgressIndicator(Mockito.eq(false));
        Mockito.verify(mSessionListView).showSessions(Mockito.eq(sessions));

    }

    @Test
    public void testLoadSessionsWhenZeroSessions() {
        mSessionListPresenter.loadSessions();

        Mockito.verify(mSessionListView).showZeroSessionsFoundMessage(Mockito.eq(false));
        Mockito.verify(mSessionListView).setProgressIndicator(Mockito.eq(true));
        Mockito.verify(mSessionService).getSessions(mSessionCallbackCaptor.capture());

        List<SessionListItem> sessions = new ArrayList<>();

        mSessionCallbackCaptor.getValue().success(sessions, null);

        Mockito.verify(mSessionListView).setProgressIndicator(Mockito.eq(false));
        Mockito.verify(mSessionListView).showZeroSessionsFoundMessage(Mockito.eq(true));

    }
}
