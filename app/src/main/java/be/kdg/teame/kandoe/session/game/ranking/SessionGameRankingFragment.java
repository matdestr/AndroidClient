package be.kdg.teame.kandoe.session.game.ranking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.session.game.DataListener;
import be.kdg.teame.kandoe.session.game.SessionGameFragment;
import butterknife.ButterKnife;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

public class SessionGameRankingFragment extends BaseFragment implements SessionGameRankingContract.View {
    private RankAdapter mRankAdapter;

    @Getter
    @Inject
    SessionGameRankingContract.UserActionsListener mGameRankingContractPresenter;

    /**
     * Listener for clicks on sessions in the RecyclerView.
     */
    private RankListener mItemListener = new RankListener() {
        @Override
        public void onCardClick(CardPosition cardPosition) {
            Log.d(RankListener.class.getSimpleName(), "Clicked on card: " + cardPosition.getCardDetails().getText());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameRankingContractPresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game_ranking, container, false);
        ButterKnife.bind(this, root);

        mRankAdapter = new RankAdapter(getContext(), new ArrayList<CardPosition>(0), mItemListener);

        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {

    }

    @Override
    public void showData(List<CardPosition> cardPositions) {
        mRankAdapter.replaceData(cardPositions);
    }

    @Override
    public void updateData(List<CardPosition> cardPositions) {
        mRankAdapter.replaceData(cardPositions);
    }


    private static class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
        private Context mContext;
        private List<CardPosition> mCardPositions;
        private RankListener mItemListener;

        public RankAdapter(Context context, List<CardPosition> cardPositions, RankListener itemListener) {
            setList(cardPositions);
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
            CardPosition cardPosition = mCardPositions.get(position);

            viewHolder.cardTitle.getBackground().setAlpha(95);

            viewHolder.cardTitle.setText(cardPosition.getCardDetails().getText());
            viewHolder.rank.setText(position + 1);
            Picasso.with(mContext)
                    .load(cardPosition.getCardDetails().getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(viewHolder.cardImage);
        }

        public void replaceData(List<CardPosition> cardPositions) {
            setList(cardPositions);
            notifyDataSetChanged();
        }

        private void setList(List<CardPosition> cardPositions) {
            mCardPositions = checkNotNull(cardPositions);
            Collections.sort(mCardPositions, new Comparator<CardPosition>() {
                @Override
                public int compare(CardPosition o1, CardPosition o2) {
                    return o1.getPriority() - o2.getPriority();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCardPositions.size();
        }

        public CardPosition getItem(int position) {
            return mCardPositions.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView rank;
            public TextView cardTitle;
            public ImageView cardImage;

            private RankListener mItemListener;

            public ViewHolder(View itemView, RankListener listener) {
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
                CardPosition cardPosition = getItem(position);
                mItemListener.onCardClick(cardPosition);
            }
        }
    }

    public interface RankListener {
        void onCardClick(CardPosition clickedCardPosition);
    }
}
