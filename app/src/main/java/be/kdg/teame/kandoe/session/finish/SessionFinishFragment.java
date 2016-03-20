package be.kdg.teame.kandoe.session.finish;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.session.SessionActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class SessionFinishFragment extends BaseFragment implements SessionFinishContract.View {
    private FinishAdapter mFinishAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private static final int GRID_SPAN_COUNT = 2;

    @Inject
    SessionFinishContract.UserActionsListener mGameFinishContractPresenter;

    private int mSessionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameFinishContractPresenter.setView(this);

        Bundle bundle = getArguments();
        mSessionId = bundle.getInt(SessionActivity.SESSION_ID, 0);

        changeToolbarTitle(R.string.session_finish_label);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game_finish, container, false);
        ButterKnife.bind(this, root);

        mFinishAdapter = new FinishAdapter(getContext(), new ArrayList<CardDetails>(0));

        recyclerView.setAdapter(mFinishAdapter);

        GridLayoutManager glm = new GridLayoutManager(getContext(), GRID_SPAN_COUNT);

        recyclerView.setLayoutManager(glm);

        mGameFinishContractPresenter.getWinners(mSessionId);

        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        DialogGenerator.showErrorDialog(getContext(), errorMessage);
    }

    @Override
    public void setCards(List<CardDetails> cards) {
        mFinishAdapter.replaceData(cards);
    }

    private static class FinishAdapter extends RecyclerView.Adapter<FinishAdapter.ViewHolder> {
        private Context mContext;
        private List<CardDetails> mCards;

        public FinishAdapter(Context context, List<CardDetails> cards) {
            setList(cards);
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View sessionView = inflater.inflate(R.layout.item_card, parent, false);

            return new ViewHolder(sessionView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            CardDetails card = mCards.get(position);

            viewHolder.cardTitle.setText(card.getText());

            Picasso.with(mContext)
                    .load(card.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(viewHolder.cardImage);
        }

        public void replaceData(List<CardDetails> cards) {
            setList(cards);
            notifyDataSetChanged();
        }

        private void setList(List<CardDetails> cards) {
            mCards = checkNotNull(cards);

            Log.d(getClass().getSimpleName(), "Received, set and sorted" + cards.size() + "card-details");
        }

        @Override
        public int getItemCount() {
            return mCards.size();
        }

        public CardDetails getItem(int position) {
            return mCards.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView cardTitle;
            public ImageView cardImage;

            public ViewHolder(View itemView) {
                super(itemView);
                cardTitle = ButterKnife.findById(itemView, R.id.session_card_item_text);
                cardImage = ButterKnife.findById(itemView, R.id.session_card_item_image);
            }
        }
    }
}
