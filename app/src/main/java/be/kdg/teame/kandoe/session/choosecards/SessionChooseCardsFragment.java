package be.kdg.teame.kandoe.session.choosecards;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

public class SessionChooseCardsFragment extends BaseFragment implements SessionChooseCardsContract.View  {
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
    SessionChooseCardsContract.UserActionsListener mAddCardsPresenter;

    private int selectedColor;
    private int unselectedColor;

    private static final int GRID_SPAN_COUNT = 2;
    private static final int ALPHA = 200;

    private int mSessionId;

    /**
     * Listener for clicks on sessions in the RecyclerView.
     */
    private SessionAddCardItemListener mItemListener = new SessionAddCardItemListener() {
        @Override
        public void onCardClick(int pos, TextView cardTitle, CheckBox checkbox) {
            mAddCardsPresenter.chooseCard(pos);

            boolean checked = checkbox.isChecked();
            checkbox.setChecked(!checked);
            checked = checkbox.isChecked();

            if (checked)
                cardTitle.setBackgroundColor(selectedColor);
            else
                cardTitle.setBackgroundColor(unselectedColor);

            cardTitle.getBackground().setAlpha(ALPHA);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddCardsPresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_choose_cards, container, false);
        ButterKnife.bind(this, root);

        this.unselectedColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        this.selectedColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);

        Bundle args = getArguments();
        mSessionId = args.getInt(SessionActivity.SESSION_ID);

        GridLayoutManager glm = new GridLayoutManager(getContext(), SessionChooseCardsFragment.GRID_SPAN_COUNT);

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
                mAddCardsPresenter.chooseCards(mSessionId);
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
        DialogGenerator.showErrorDialog(getActivity(), errorMessage);
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
    public void onChooseCardsCompleted() {
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
            View sessionView = inflater.inflate(R.layout.item_card_select, parent, false);

            return new ViewHolder(sessionView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            CardDetails cardDetails = mCardDetails.get(position);
            int statusDrawableId = R.drawable.ic_unknown;

            viewHolder.cardTitle.getBackground().setAlpha(ALPHA);
            viewHolder.cardTitle.setText(cardDetails.getText());
            Picasso.with(mContext)
                    .load(cardDetails.getImageUrl())
                    .placeholder(statusDrawableId)
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

            public RelativeLayout container;
            public TextView cardTitle;
            public ImageView cardImage;
            public CheckBox checkbox;

            private SessionAddCardItemListener mItemListener;

            public ViewHolder(View itemView, SessionAddCardItemListener listener) {
                super(itemView);
                mItemListener = listener;
                container = ButterKnife.findById(itemView, R.id.session_card_container);
                cardTitle = ButterKnife.findById(itemView, R.id.session_card_item_text);
                cardImage = ButterKnife.findById(itemView, R.id.session_card_item_image);
                checkbox = ButterKnife.findById(itemView, R.id.session_card_item_checkbox);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                mItemListener.onCardClick(position, cardTitle, checkbox);
            }
        }
    }

    public interface SessionAddCardItemListener {
        void onCardClick(int pos, TextView textView, CheckBox checkBox);
    }
}
