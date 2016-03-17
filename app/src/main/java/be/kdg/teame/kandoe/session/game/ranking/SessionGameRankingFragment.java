package be.kdg.teame.kandoe.session.game.ranking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import butterknife.ButterKnife;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

public class SessionGameRankingFragment extends BaseFragment implements SessionGameRankingContract.View {
    @Getter
    private SessionGameRankingContract.UserActionsListener mGameRankingContractPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game_ranking, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }

    private static class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
        private Context mContext;
        private List<CardDetails> mCardDetails;
        private SessionRankListener mItemListener;

        public RankAdapter(Context context, List<CardDetails> sessions, SessionRankListener itemListener) {
            setList(sessions);
            mContext = context;
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View sessionView = inflater.inflate(R.layout.item_rank, parent, false);

            return new ViewHolder(sessionView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            CardDetails cardDetails = mCardDetails.get(position);

            viewHolder.cardTitle.getBackground().setAlpha(95);

            viewHolder.cardTitle.setText(cardDetails.getText());
            viewHolder.rank.setText(position + 1);
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

            public TextView rank;
            public TextView cardTitle;
            public ImageView cardImage;

            private SessionRankListener mItemListener;

            public ViewHolder(View itemView, SessionRankListener listener) {
                super(itemView);
                mItemListener = listener;
                rank = ButterKnife.findById(itemView, R.id.rank);
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

    public interface SessionRankListener {
        void onCardClick(CardDetails clickedCard);
    }
}
