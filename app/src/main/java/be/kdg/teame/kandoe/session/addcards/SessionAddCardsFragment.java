package be.kdg.teame.kandoe.session.addcards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.session.SessionActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class SessionAddCardsFragment extends BaseFragment implements SessionAddCardsContract.View {
    private CardAdapter mCardAdapter;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.fab_ok)
    FloatingActionButton mFloatingActionButton;

    @Bind(R.id.session_wait_container)
    RelativeLayout mWaitContainer;

    @Bind(R.id.wait_progressbar)
    ProgressBar mWaitProgressBar;

    @Inject
    SessionAddCardsContract.UserActionsListener mAddCardsPresenter;

    private static final int GRID_SPAN_COUNT = 2;

    private int mSessionId;

    /**
     * Listener for clicks on sessions in the RecyclerView.
     */
    private SessionAddCardItemListener mItemListener = new SessionAddCardItemListener() {
        @Override
        public void onCardClick(CardDetails clickedCard) {
            Log.d(SessionAddCardItemListener.class.getSimpleName(), "Clicked on card: " + clickedCard.getText());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddCardsPresenter.setView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_session_add_card, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_session_add_card:
                Log.i("test", "test");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_cards, container, false);
        ButterKnife.bind(this, root);

        Bundle args = getArguments();
        mSessionId = args.getInt(SessionActivity.SESSION_ID);

        GridLayoutManager glm = new GridLayoutManager(getContext(), SessionAddCardsFragment.GRID_SPAN_COUNT);

        mCardAdapter = new CardAdapter(getContext(), new ArrayList<CardDetails>(0), mItemListener);

        mRecyclerView.setAdapter(mCardAdapter);
        mRecyclerView.setLayoutManager(glm);

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAddCardsPresenter.loadCards(mSessionId);
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAddCardsPresenter.loadCards(mSessionId);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        if (errorMessage != null)
            DialogGenerator.showErrorDialog(getActivity(), errorMessage);
        else
            DialogGenerator.showErrorDialog(getActivity());
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
    public void showCards(List<CardDetails> cardDetails) {
        mCardAdapter.replaceData(cardDetails);
    }

    @Override
    public void onCardsAddedCompleted() {
        mFloatingActionButton.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mWaitProgressBar.setIndeterminate(true);
        mWaitContainer.setVisibility(View.VISIBLE);
    }

    private static class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
        private Context mContext;
        private List<CardDetails> mCardDetails;
        private SessionAddCardItemListener mItemListener;

        public CardAdapter(Context context, List<CardDetails> sessions, SessionAddCardItemListener itemListener) {
            setList(sessions);
            mContext = context;
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View sessionView = inflater.inflate(R.layout.item_card, parent, false);

            return new ViewHolder(sessionView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            CardDetails cardDetails = mCardDetails.get(position);

            viewHolder.cardTitle.getBackground().setAlpha(95);

            viewHolder.cardTitle.setText(cardDetails.getText());
            Picasso.with(mContext)
                    .load(cardDetails.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(viewHolder.cardImage);
        }

        public void replaceData(List<CardDetails> sessions) {
            setList(sessions);
            notifyDataSetChanged();
        }

        private void setList(List<CardDetails> sessions) {
            mCardDetails = checkNotNull(sessions);
        }

        @Override
        public int getItemCount() {
            return mCardDetails.size();
        }

        public CardDetails getItem(int position) {
            return mCardDetails.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView cardTitle;
            public ImageView cardImage;

            private SessionAddCardItemListener mItemListener;

            public ViewHolder(View itemView, SessionAddCardItemListener listener) {
                super(itemView);
                mItemListener = listener;
                cardTitle = ButterKnife.findById(itemView, R.id.session_card_item_text);
                cardImage = ButterKnife.findById(itemView, R.id.session_card_item_image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                CardDetails session = getItem(position);
                mItemListener.onCardClick(session);
            }
        }
    }

    public interface SessionAddCardItemListener {
        void onCardClick(CardDetails clickedCard);
    }
}
