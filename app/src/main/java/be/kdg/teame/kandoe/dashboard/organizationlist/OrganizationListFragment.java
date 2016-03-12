package be.kdg.teame.kandoe.dashboard.organizationlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.organizations.Organization;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;


public class OrganizationListFragment extends BaseFragment implements OrganizationListContract.View {

    private OrganizationAdapter mListAdapter;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.nothing_to_show_container)
    LinearLayout mNothingToShowContainer;

    @Inject
    OrganizationListContract.UserActionsListener mOrganizationListPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrganizationListPresenter.setView(this);
        mListAdapter = new OrganizationAdapter(new ArrayList<Organization>(0));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_organization_list, container, false);
        ButterKnife.bind(this, root);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mListAdapter);

        // Pull-to-refresh
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mOrganizationListPresenter.loadOrganizations();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mOrganizationListPresenter.loadOrganizations();
    }

    @Override
    public void setProgressIndicator(final boolean active) {

        if (getView() == null || mSwipeRefreshLayout == null)
            return;

        // Make sure setRefreshing() is called after the layout is done with everything else.
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showOrganizations(List<Organization> organizations) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mListAdapter.replaceData(organizations);
    }

    @Override
    public void showZeroOrganizationsFoundMessage(boolean active) {
        if (active) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mNothingToShowContainer.setVisibility(View.VISIBLE);
        } else {
            mNothingToShowContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        DialogGenerator.showErrorDialog(getContext(), errorMessage);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    private static class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolder> {

        private List<Organization> mOrganizations;

        public OrganizationAdapter(List<Organization> organizations) {
            setList(organizations);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View sessionView = inflater.inflate(R.layout.item_organization, parent, false);

            return new ViewHolder(sessionView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Organization organization = mOrganizations.get(position);

            viewHolder.title.setText(organization.getName());
        }

        public void replaceData(List<Organization> organizations) {
            setList(organizations);
            notifyDataSetChanged();
        }

        private void setList(List<Organization> organizations) {
            mOrganizations = checkNotNull(organizations);
        }

        @Override
        public int getItemCount() {
            return mOrganizations.size();
        }

        public Organization getItem(int position) {
            return mOrganizations.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = ButterKnife.findById(itemView, R.id.organization_title);
            }
        }
    }
}