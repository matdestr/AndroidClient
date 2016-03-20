package be.kdg.teame.kandoe.session.reviewcards;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.sessions.dto.CreateReviewDTO;
import be.kdg.teame.kandoe.session.SessionActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionReviewCardsFragment extends BaseFragment implements SessionReviewCardsContract.View {
    private static final int ALPHA = 200;
    private static final int GRID_SPAN_COUNT = 2;

    private int mSessionId;
    private CardAdapter mCardAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.fab_continue)
    FloatingActionButton mFloatingActionButton;

    @Bind(R.id.session_wait_container)
    RelativeLayout mWaitContainer;

    @Bind(R.id.wait_progressbar)
    ProgressBar mWaitProgressBar;

    @Inject
    SessionReviewCardsContract.UserActionsListener mReviewCardsPresenter;

    private ProgressDialog mProgressDialog;

    private SessionReviewCardClickListener mCardClickListener = new SessionReviewCardClickListener() {
        @Override
        public void onCardClick(final CardDetails clickedCard) {
            final CreateReviewDTO review = new CreateReviewDTO();
            AddReviewDialog dialog = new AddReviewDialog(getContext(), clickedCard, review);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mReviewCardsPresenter.saveReview(clickedCard, review.getMessage());
                }
            });

            dialog.show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewCardsPresenter.setView(this);
        mSessionId = getArguments().getInt(SessionActivity.SESSION_ID);

        changeToolbarTitle(R.string.session_review_cards_label);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_review_cards, container, false);
        ButterKnife.bind(this, root);

        Bundle args = getArguments();
        mSessionId = args.getInt(SessionActivity.SESSION_ID);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), GRID_SPAN_COUNT);
        mCardAdapter = new CardAdapter(getContext(), new ArrayList<CardDetails>(0), mCardClickListener);

        mRecyclerView.setAdapter(mCardAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mProgressDialog = DialogGenerator.createProgressDialog(getContext(), R.string.saving_reviews);

        mReviewCardsPresenter.loadCards(mSessionId);

        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @OnClick(R.id.fab_continue)
    void onContinueClick() {
        mReviewCardsPresenter.postReviews(mSessionId);
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
    public void showCards(List<CardDetails> cardDetails) {
        mCardAdapter.replaceData(cardDetails);
    }

    @Override
    public void showWaitingForOtherParticipants() {
        mRecyclerView.setVisibility(View.GONE);
        mFloatingActionButton.setVisibility(View.GONE);
        mWaitProgressBar.setIndeterminate(true);
        mWaitContainer.setVisibility(View.VISIBLE);
    }

    private static class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
        private Context mContext;
        private List<CardDetails> mCards;
        private SessionReviewCardClickListener mItemListener;

        public CardAdapter(Context mContext, List<CardDetails> mCards, SessionReviewCardClickListener mItemListener) {
            this.mContext = mContext;
            this.mCards = Preconditions.checkNotNull(mCards);
            this.mItemListener = mItemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context parentContext = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(parentContext);
            View view = inflater.inflate(R.layout.item_card, parent, false);

            return new ViewHolder(view, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CardDetails cardDetails = mCards.get(position);

            holder.cardText.getBackground().setAlpha(SessionReviewCardsFragment.ALPHA);
            holder.cardText.setText(cardDetails.getText());

            Picasso.with(mContext)
                    .load(cardDetails.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.cardImage);
        }

        public void replaceData(List<CardDetails> cards) {
            mCards = Preconditions.checkNotNull(cards);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mCards.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private RelativeLayout container;
            private TextView cardText;
            private ImageView cardImage;
            private SessionReviewCardClickListener mListener;

            public ViewHolder(View itemView, SessionReviewCardClickListener listener) {
                super(itemView);
                mListener = listener;
                container = ButterKnife.findById(itemView, R.id.session_card_container);
                cardText = ButterKnife.findById(itemView, R.id.session_card_item_text);
                cardImage = ButterKnife.findById(itemView, R.id.session_card_item_image);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                CardDetails cardDetails = mCards.get(position);

                mListener.onCardClick(cardDetails);
            }
        }
    }

    private interface SessionReviewCardClickListener {
        void onCardClick(CardDetails clickedCard);
    }
}
