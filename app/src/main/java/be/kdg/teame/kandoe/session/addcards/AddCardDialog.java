package be.kdg.teame.kandoe.session.addcards;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.models.cards.CardDetails;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddCardDialog extends Dialog {
    View container;
    ImageView mCardImageView;
    EditText mCardTextView;
    Button mAcceptButton, mDeclineButton;

    public AddCardDialog(final Activity parent, final CardDetails details) {
        super(parent);
        setContentView(R.layout.dialog_add_card);

        container = ButterKnife.findById(this, R.id.add_card_view);
        container.getBackground().setAlpha(240);

        mCardTextView = ButterKnife.findById(this, R.id.session_card_item_text);

        mCardImageView = ButterKnife.findById(this, R.id.session_card_item_image);
        mCardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                parent.startActivityForResult(i, 1);
            }
        });

        mAcceptButton = ButterKnife.findById(this, R.id.action_dialog_accept);
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mCardTextView.getText().toString();
                if (! text.isEmpty()) {
                    details.setText(text);
                    AddCardDialog.this.dismiss();
                }
            }
        });

        mDeclineButton = ButterKnife.findById(this, R.id.action_dialog_decline);
        mDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setText(null);
                AddCardDialog.this.dismiss();
            }
        });
    }
}
