package be.kdg.teame.kandoe.core;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import be.kdg.teame.kandoe.R;

/**
 * Helper class with static methods that allows you to create error and progress dialogs.
 */
public class DialogGenerator {

    // error dialogs (string resources)

    public static void showErrorDialog(Context context, @StringRes int titleId, @StringRes int messageId, DialogInterface.OnClickListener onPositiveClickListener) {
        if (context != null)
            showErrorDialog(context, context.getString(titleId), context.getString(messageId), onPositiveClickListener);
    }

    public static void showErrorDialog(Context context, @StringRes int messageId, DialogInterface.OnClickListener onPositiveClickListener) {
        if (context != null)
            showErrorDialog(context, context.getString(R.string.dialog_error_title_default), context.getString(messageId), onPositiveClickListener);
    }

    public static void showErrorDialog(Context context, @StringRes int titleId, @StringRes int messageId) {
        if (context != null)
            showErrorDialog(context, context.getString(titleId), context.getString(messageId), null);
    }

    public static void showErrorDialog(Context context, @StringRes int messageId) {
        if (context != null) {
            if (messageId == 0)
                showErrorDialog(context);
            else
                showErrorDialog(context, context.getString(R.string.dialog_error_title_default), context.getString(messageId), null);
        }
    }


    // error dialogs (strings)

    public static void showErrorDialog(Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener onPositiveClickListener) {
        if (context != null)
            createErrorDialog(context, title, message, onPositiveClickListener).show();
    }

    public static void showErrorDialog(Context context, CharSequence title, CharSequence message) {
        if (context != null)
            showErrorDialog(context, title, message, null);
    }

    public static void showErrorDialog(Context context, CharSequence message) {
        if (message != null)
            showErrorDialog(context, context.getString(R.string.dialog_error_title_default), message, null);
        else
            showErrorDialog(context);
    }

    public static void showErrorDialog(Context context) {
        if (context != null)
            showErrorDialog(context, context.getString(R.string.dialog_error_title_default), context.getString(R.string.dialog_error_message_default));
    }


    public static Dialog createErrorDialog(Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok, onPositiveClickListener);

        return builder.create();
    }


    // process dialogs

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context, @StringRes int messageId) {
        ProgressDialog progressDialog = createProgressDialog(context);
        progressDialog.setMessage(context.getString(messageId));

        return progressDialog;
    }

}
