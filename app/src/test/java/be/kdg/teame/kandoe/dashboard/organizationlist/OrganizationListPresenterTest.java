package be.kdg.teame.kandoe.dashboard.organizationlist;

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

import be.kdg.teame.kandoe.dashboard.sessionlist.SessionListPresenter;
import be.kdg.teame.kandoe.data.retrofit.services.OrganizationService;
import be.kdg.teame.kandoe.models.organizations.Organization;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;
import be.kdg.teame.kandoe.models.users.User;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.Callback;

@RunWith(MockitoJUnitRunner.class)
public class OrganizationListPresenterTest {
    @Mock
    private OrganizationListContract.View mOrganizationListView;

    private OrganizationListContract.UserActionsListener mOrganizationListPresenter;

    @Mock
    private OrganizationService mOrganizationService;

    @Mock
    private PrefManager mPrefManager;

    @Captor
    private ArgumentCaptor<Callback<List<Organization>>> mOrganizationCallbackCaptor;

    @Before
    public void setUp() {
        mOrganizationListPresenter = new OrganizationListPresenter(mOrganizationService, mPrefManager);
        mOrganizationListPresenter.setView(mOrganizationListView);
    }

    @Test
    public void testLoadOrganizations() {
        Mockito.when(mPrefManager.retrieveUsername()).thenReturn("username");

        mOrganizationListPresenter.loadOrganizations();

        Mockito.verify(mOrganizationListView).showZeroOrganizationsFoundMessage(false);
        Mockito.verify(mOrganizationListView).setProgressIndicator(Mockito.eq(true));
        Mockito.verify(mOrganizationService).getOrganizations(
                Mockito.eq("username"),
                Mockito.eq(true),
                mOrganizationCallbackCaptor.capture()
        );

        List<Organization> organizations = new ArrayList<>();
        organizations.add(new Organization());

        mOrganizationCallbackCaptor.getValue().success(organizations, null);

        Mockito.verify(mOrganizationListView).setProgressIndicator(Mockito.eq(false));
        Mockito.verify(mOrganizationListView).showOrganizations(Mockito.eq(organizations));

    }


    @Test
    public void testLoadOrganizationsWhenZeroOrganizations() {
        Mockito.when(mPrefManager.retrieveUsername()).thenReturn("username");

        mOrganizationListPresenter.loadOrganizations();

        Mockito.verify(mOrganizationListView).showZeroOrganizationsFoundMessage(false);
        Mockito.verify(mOrganizationListView).setProgressIndicator(Mockito.eq(true));
        Mockito.verify(mOrganizationService).getOrganizations(
                Mockito.eq("username"),
                Mockito.eq(true),
                mOrganizationCallbackCaptor.capture()
        );

        List<Organization> organizations = new ArrayList<>();

        mOrganizationCallbackCaptor.getValue().success(organizations, null);

        Mockito.verify(mOrganizationListView).setProgressIndicator(Mockito.eq(false));
        Mockito.verify(mOrganizationListView).showZeroOrganizationsFoundMessage(true);

    }
}
