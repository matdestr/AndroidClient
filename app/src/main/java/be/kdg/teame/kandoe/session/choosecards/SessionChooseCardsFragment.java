package be.kdg.teame.kandoe.session.choosecards;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class SessionChooseCardsFragment extends BaseFragment implements SessionChooseCardsContract.View {

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.fab_continue)
    FloatingActionButton mFloatingActionButton;

    @Bind(R.id.session_wait_container)
    RelativeLayout mWaitContainer;

    @Bind(R.id.wait_progressbar)
    ProgressBar mWaitProgressBar;

    private ProgressDialog mProgressDialog;

    @Inject
    SessionChooseCardsContract.UserActionsListener mAddCardsPresenter;

    private CardAdapter mCardAdapter;
    private int mSessionId;

    private static final int GRID_SPAN_COUNT = 2;
    private static final int ALPHA = 200;

    /**
     * Listener for clicks on sessions in the RecyclerView.
     */
    private SessionAddCardItemListener mItemListener = new SessionAddCardItemListener() {
        @Override
        public void onCardClick(CardDetails clickedCard) {
            clickedCard.setActive(!clickedCard.isActive());
            mCardAdapter.notifyDataSetChanged();
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

        mProgressDialog = DialogGenerator.createProgressDialog(getContext(), R.string.session_saving_cards);


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
    public void setProgressIndicator(boolean active) {
        if (active)
            mProgressDialog.show();
        else
            mProgressDialog.dismiss();
    }

    @Override
    public void setRefreshingProgressIndicator(final boolean active) {

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
    public void showWaitingForOtherParticipants() {
        mFloatingActionButton.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mWaitProgressBar.setIndeterminate(true);
        mWaitContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoCardsSelected() {
        if (getView() == null) return;

        Snackbar snackbar = Snackbar.make(getView(), R.string.no_cards_selected, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = ButterKnife.findById(sbView, android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextLight));

        snackbar.show();
    }

    @OnClick(R.id.fab_continue)
    public void onFabContinueClick() {
        List<Integer> chosenCards = new ArrayList<>();

        for (CardDetails card : mCardAdapter.getData())
            if (card.isActive())
                chosenCards.add(card.getCardDetailsId());


        mAddCardsPresenter.chooseCards(mSessionId, chosenCards);
    }

    private static class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
        private Context mContext;
        private List<CardDetails> mCards;
        private SessionAddCardItemListener mItemListener;

        public CardAdapter(Context context, List<CardDetails> cards, SessionAddCardItemListener itemListener) {
            setList(cards);
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
            CardDetails cardDetails = mCards.get(position);

            viewHolder.cardTitle.getBackground().setAlpha(ALPHA);
            viewHolder.cardTitle.setText(cardDetails.getText());
            Picasso.with(mContext)
                    .load(cardDetails.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(viewHolder.cardImage);

            viewHolder.checkbox.setChecked(cardDetails.isActive());

            if (cardDetails.isActive())
                viewHolder.cardTitle.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            else
                viewHolder.cardTitle.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }

        public void replaceData(List<CardDetails> cards) {
            setList(cards);
            notifyDataSetChanged();
        }

        public List<CardDetails> getData() {
            return mCards;
        }

        private void setList(List<CardDetails> cards) {
            mCards = checkNotNull(cards);
        }

        @Override
        public int getItemCount() {
            return mCards.size();
        }

        public CardDetails getItem(int position) {
            return mCards.get(position);
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
                CardDetails card = getItem(position);
                mItemListener.onCardClick(card);
            }
        }
    }

    public interface SessionAddCardItemListener {
        void onCardClick(CardDetails clickedCard);
    }
}
