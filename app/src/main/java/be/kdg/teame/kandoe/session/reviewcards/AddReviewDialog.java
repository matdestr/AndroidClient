package be.kdg.teame.kandoe.session.reviewcards;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import be.kdg.teame.kandoe.models.sessions.dto.CreateReviewDTO;
import butterknife.ButterKnife;

public class AddReviewDialog extends Dialog {
    View container;
    ImageView mCardImageView;
    TextView mCardTextView;
    EditText mCardReviewEditText;
    Button mAcceptButton;
    Button mDeclineButton;

    public AddReviewDialog(final Context context, final CardDetails cardDetails, final CreateReviewDTO review) {
        super(context);

        setContentView(R.layout.dialog_review_card);

        container = ButterKnife.findById(this, R.id.review_card_view);
        container.getBackground().setAlpha(240);

        mCardTextView = ButterKnife.findById(this, R.id.session_card_item_text);
        mCardImageView = ButterKnife.findById(this, R.id.session_card_item_image);
        mCardReviewEditText = ButterKnife.findById(this, R.id.session_review_text);
        mAcceptButton = ButterKnife.findById(this, R.id.action_dialog_accept);
        mDeclineButton = ButterKnife.findById(this, R.id.action_dialog_decline);

        mCardTextView.setText(cardDetails.getText());

        Picasso.with(context)
                .load(cardDetails.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(mCardImageView);

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = mCardReviewEditText.getText().toString();
                review.setMessage(reviewText);
                AddReviewDialog.this.dismiss();
            }
        });

        mDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setMessage("");
                AddReviewDialog.this.dismiss();
            }
        });
    }
}
