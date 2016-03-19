package be.kdg.teame.kandoe.session.game.picker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.session.SessionActivity;
import be.kdg.teame.kandoe.session.game.ChildFragmentReadyListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Getter;
import lombok.Setter;

public class SessionGamePickerFragment extends BaseFragment implements SessionGamePickerContract.View {

    @Bind(R.id.card_image)
    ImageView mCardImageView;

    @Bind(R.id.card_text)
    TextView mCardTextView;

    @Getter
    @Inject
    SessionGamePickerContract.UserActionsListener mSessionGamePickerPresenter;

    private ProgressDialog mProgressDialog;
    private ChildFragmentReadyListener mFragmentReadyListener;

    private int mSessionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionGamePickerPresenter.setView(this);

        Bundle bundle = getArguments();
        mSessionId = bundle.getInt(SessionActivity.SESSION_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game_picker, container, false);
        ButterKnife.bind(this, root);

        mProgressDialog = DialogGenerator.createProgressDialog(getContext(), R.string.processing_vote);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mFragmentReadyListener != null)
            mFragmentReadyListener.onReadyToListen(mSessionGamePickerPresenter);

    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

    @OnClick(R.id.fab_next_card)
    public void onFabNextCardClick() {
        mSessionGamePickerPresenter.loadNextCard();
    }

    @OnClick(R.id.fab_previous_card)
    public void onFabPreviousCardClick() {
        mSessionGamePickerPresenter.loadPreviousCard();
    }

    @OnClick(R.id.fab_pick_card)
    public void onFabPickCardClick() {
        mSessionGamePickerPresenter.pickCard(mSessionId);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active)
            mProgressDialog.show();
        else
            mProgressDialog.dismiss();
    }

    @Override
    public void showNextCard(CardDetails cardDetails) {
        showCard(cardDetails);
    }

    @Override
    public void showPreviousCard(CardDetails cardDetails) {
        showCard(cardDetails);
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

    @Override
    public void showCard(CardDetails cardDetails) {
        Picasso.with(getContext())
                .load(cardDetails.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(mCardImageView);

        mCardTextView.setText(cardDetails.getText());
    }

    public void setFragmentReadyListener(ChildFragmentReadyListener fragmentReadyListener) {
        this.mFragmentReadyListener = fragmentReadyListener;
    }
}
