package be.kdg.teame.kandoe.dashboard.sessionlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.sessions.SessionListItem;
import be.kdg.teame.kandoe.session.SessionActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;


public class SessionListFragment extends BaseFragment implements SessionListContract.View {

    private SessionsAdapter mListAdapter;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.nothing_to_show_container)
    LinearLayout mNothingToShowContainer;

    @Inject
    SessionListContract.UserActionsListener mSessionOverviewPresenter;

    /**
     * Listener for clicks on sessions in the RecyclerView.
     */
    private SessionItemListener mItemListener = new SessionItemListener() {
        @Override
        public void onSessionClick(SessionListItem clickedSession) {
            mSessionOverviewPresenter.openSession(clickedSession);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionOverviewPresenter.setView(this);
        mListAdapter = new SessionsAdapter(new ArrayList<SessionListItem>(0), mItemListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_list, container, false);
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
                mSessionOverviewPresenter.loadSessions();
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSessionOverviewPresenter.loadSessions();
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
    public void showSessions(List<SessionListItem> sessions) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mListAdapter.replaceData(sessions);
    }

    @Override
    public void showZeroSessionsFoundMessage(boolean active) {
        if (active) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mNothingToShowContainer.setVisibility(View.VISIBLE);
        } else {
            mNothingToShowContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSessionDetail(SessionListItem session) {
        Intent intent = new Intent(getContext(), SessionActivity.class);
        intent.putExtra(SessionActivity.SESSION_ID, session.getSessionId());
        intent.putExtra(SessionActivity.SESSION_CATEGORY_TITLE, session.getCategoryTitle());
        intent.putExtra(SessionActivity.SESSION_TOPIC_TITLE, session.getTopicTitle());
        intent.putExtra(SessionActivity.SESSION_ORGANIZATION_TITLE, session.getOrganizationTitle());

        startActivity(intent);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        DialogGenerator.showErrorDialog(getContext(), R.string.error_connection_failure);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    private static class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.ViewHolder> {

        private List<SessionListItem> mSessions;
        private SessionItemListener mItemListener;

        public SessionsAdapter(List<SessionListItem> sessions, SessionItemListener itemListener) {
            setList(sessions);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View sessionView = inflater.inflate(R.layout.item_session, parent, false);

            return new ViewHolder(sessionView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            SessionListItem session = mSessions.get(position);

            int statusDrawableId = R.drawable.ic_unknown;

            if (session.getSessionStatus() != null) {

                switch (session.getSessionStatus()) {
                    case CREATED:
                        statusDrawableId = R.drawable.ic_session_status_created;
                        break;
                    case USERS_JOINING:
                        statusDrawableId = R.drawable.ic_session_status_users_joining;
                        break;
                    case ADDING_CARDS:
                        statusDrawableId = R.drawable.ic_session_status_adding_cards;
                        break;
                    case REVIEWING_CARDS:
                        statusDrawableId = R.drawable.ic_session_status_reviewing_cards;
                        break;
                    case CHOOSING_CARDS:
                        statusDrawableId = R.drawable.ic_session_status_choosing_cards;
                        break;
                    case READY_TO_START:
                        statusDrawableId = R.drawable.ic_session_status_ready;
                        break;
                    case IN_PROGRESS:
                        statusDrawableId = R.drawable.ic_session_status_in_progess;
                        break;
                    case FINISHED:
                        statusDrawableId = R.drawable.ic_session_status_finished;
                        break;
                    default:
                        statusDrawableId = R.drawable.ic_unknown;
                }

            }

            viewHolder.title.setText(String.format("Session %d", session.getSessionId()));
            viewHolder.status.setImageResource(statusDrawableId);
            viewHolder.organization.setText(session.getOrganizationTitle());
            viewHolder.category.setText(session.getCategoryTitle());

            if (session.getTopicTitle() != null)
                viewHolder.topic.setText(session.getTopicTitle());
            else
                viewHolder.topicContainer.setVisibility(View.INVISIBLE);

            viewHolder.participantAmount.setText(String.valueOf(session.getParticipantAmount()));

        }

        public void replaceData(List<SessionListItem> sessions) {
            setList(sessions);
            notifyDataSetChanged();
        }

        private void setList(List<SessionListItem> sessions) {
            mSessions = checkNotNull(sessions);
        }

        @Override
        public int getItemCount() {
            return mSessions.size();
        }

        public SessionListItem getItem(int position) {
            return mSessions.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView title;
            public ImageView status;
            public TextView organization;
            public TextView category;
            public TextView topic;
            public TextView participantAmount;
            public View topicContainer;

            private SessionItemListener mItemListener;

            public ViewHolder(View itemView, SessionItemListener listener) {
                super(itemView);
                mItemListener = listener;
                title = ButterKnife.findById(itemView, R.id.session_title);
                status = ButterKnife.findById(itemView, R.id.session_status_icon);
                organization = ButterKnife.findById(itemView, R.id.organization_title);
                category = ButterKnife.findById(itemView, R.id.category_title);
                topic = ButterKnife.findById(itemView, R.id.topic_title);
                participantAmount = ButterKnife.findById(itemView, R.id.session_participant_amount);
                topicContainer = ButterKnife.findById(itemView, R.id.topic_container);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                SessionListItem session = getItem(position);
                mItemListener.onSessionClick(session);
            }
        }
    }

    public interface SessionItemListener {

        void onSessionClick(SessionListItem clickedSession);
    }

}