package be.kdg.teame.kandoe.session.game.ranking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.session.SessionActivity;
import be.kdg.teame.kandoe.session.game.ChildFragmentReadyListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import lombok.Setter;

import static com.google.common.base.Preconditions.checkNotNull;

public class SessionGameRankingFragment extends BaseFragment implements SessionGameRankingContract.View {
    private RankAdapter mRankAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.fab_finish)
    FloatingActionButton mFabFinish;

    @Getter
    @Inject
    SessionGameRankingContract.UserActionsListener mGameRankingContractPresenter;

    private ProgressDialog mProgressDialog;
    private ChildFragmentReadyListener mFragmentReadyListener;
    private int sessionId;
    private boolean isOrganizer;

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

        Bundle args = getArguments();
        isOrganizer = args.getBoolean(SessionActivity.SESSION_IS_ORGANIZER, false);
        sessionId = args.getInt(SessionActivity.SESSION_ID);
        Log.d(getClass().getSimpleName(), "isOrganizer: " + isOrganizer);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game_ranking, container, false);
        ButterKnife.bind(this, root);

        mRankAdapter = new RankAdapter(getContext(), new ArrayList<CardPosition>(0), mItemListener);
        recyclerView.setAdapter(mRankAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressDialog = DialogGenerator.createProgressDialog(getContext(), R.string.ending_game);

        if (isOrganizer)
            mFabFinish.setVisibility(View.VISIBLE);
        else
            mFabFinish.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mFragmentReadyListener != null)
            mFragmentReadyListener.onReadyToListen(mGameRankingContractPresenter);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        if (getView() == null) return;

        Snackbar snackbar = Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = ButterKnife.findById(sbView, android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextLight));

        snackbar.show();
    }

    @OnClick(R.id.fab_finish)
    void endGame(){
        if (isOrganizer){
            Log.d(getClass().getSimpleName(), "ending game");
            mGameRankingContractPresenter.endGame(sessionId);
        }
    }

    @Override
    public void showData(List<CardPosition> cardPositions) {
        mRankAdapter.replaceData(cardPositions);
        Log.d(getClass().getSimpleName(), "Replaced cardpositions");
    }

    @Override
    public void updateData(List<CardPosition> cardPositions) {
        mRankAdapter.replaceData(cardPositions);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active)
            mProgressDialog.show();
        else
            mProgressDialog.dismiss();
    }


    public void setFragmentReadyListener(ChildFragmentReadyListener fragmentReadyListener) {
        this.mFragmentReadyListener = fragmentReadyListener;
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

            viewHolder.cardTitle.setText(cardPosition.getCardDetails().getText());
            viewHolder.rank.setText("" + (position + 1));
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
            Log.d(getClass().getSimpleName(), "Received, set and sorted" + cardPositions.size() + "cardpositions");
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
