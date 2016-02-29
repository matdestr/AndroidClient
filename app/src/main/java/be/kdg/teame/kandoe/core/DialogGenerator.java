package be.kdg.teame.kandoe.core;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import be.kdg.teame.kandoe.R;

/**
 * Generates different kinds of dialogs (e.g. error, progress ...).
 * You can just create a dialog, but there is also the possibility to show some of them directly.
 */
public class DialogGenerator {

    public static void showErrorDialog(Context context, @StringRes int titleId, @StringRes int messageId, DialogInterface.OnClickListener onPositiveClickListener){
        showErrorDialog(context, context.getString(titleId), context.getString(messageId), onPositiveClickListener);
    }

    public static void showErrorDialog(Context context, @StringRes int messageId, DialogInterface.OnClickListener onPositiveClickListener){
        showErrorDialog(context, context.getString(R.string.dialog_error_title_default), context.getString(messageId), onPositiveClickListener);
    }

    public static void showErrorDialog(Context context, @StringRes int titleId, @StringRes int messageId){
        showErrorDialog(context, context.getString(titleId), context.getString(messageId), null);
    }

    public static void showErrorDialog(Context context, @StringRes int messageId){
        showErrorDialog(context, context.getString(R.string.dialog_error_title_default), context.getString(messageId), null);
    }


    public static void showErrorDialog(Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener onPositiveClickListener) {
        createErrorDialog(context, title, message, onPositiveClickListener).show();
    }

    public static void showErrorDialog(Context context, CharSequence title, CharSequence message) {
        showErrorDialog(context, title, message, null);
    }

    public static void showErrorDialog(Context context, CharSequence message) {
        showErrorDialog(context, context.getString(R.string.dialog_error_title_default), message, null);
    }


    public static Dialog createErrorDialog(Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener onPositiveClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok, onPositiveClickListener);

        return builder.create();
    }

    public static ProgressDialog createProgressDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context, @StringRes int messageId){
        ProgressDialog progressDialog = createProgressDialog(context);
        progressDialog.setMessage(context.getString(messageId));

        return progressDialog;
    }

}
