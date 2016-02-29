package be.kdg.teame.kandoe.util.validators.forms;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;

import be.kdg.teame.kandoe.R;

public class FormField {
    public enum Type {
        USER_NAME, TEXT, EMAIL, PASSWORD, VERIFY;
    }

    private TextInputLayout textInputLayout;
    private TextView textView;
    private Type type;
    private boolean required;
    private TextView compareTextView;


    private Context context;


    public FormField(TextInputLayout textInputLayout, TextView textView) {
        this(textInputLayout, textView, Type.TEXT);
    }

    public FormField(TextInputLayout textInputLayout, TextView textView, boolean required) {
        this(textInputLayout, textView, Type.TEXT, required);
    }

    public FormField(TextInputLayout textInputLayout, TextView textView, Type type) {
        this(textInputLayout, textView, type, false);
    }

    public FormField(TextInputLayout textInputLayout, TextView textView, Type type, boolean required) {
        this(textInputLayout, textView, type, required, null);
    }

    public FormField(TextInputLayout textInputLayout, TextView textView, Type type, boolean required, TextView compareTextView) {
        this.textInputLayout = textInputLayout;
        this.textView = textView;
        this.type = type;
        this.required = required;
        this.compareTextView = compareTextView;
        this.context = this.textInputLayout.getContext();
    }


    public boolean validate() {
        if (required)
            if (textView.getText().toString().isEmpty()) {
                Log.i("test", "test");
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getString(R.string.error_field_required));
                return false;
            }

        switch (type) {

            case USER_NAME:

                //todo rules username

                break;

            case PASSWORD:

                //todo rules password

                break;

            case EMAIL:

                if (!Patterns.EMAIL_ADDRESS.matcher(textView.getText()).matches()) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getString(R.string.error_invalid_email));
                    return false;
                }

                break;

            case VERIFY:

                if (!textView.getText().toString().equals(compareTextView.getText().toString())) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(context.getString(R.string.error_verify));
                    return false;
                }

                break;
        }

        textInputLayout.setErrorEnabled(false);
        textInputLayout.setError(null);
        return true;
    }
}
