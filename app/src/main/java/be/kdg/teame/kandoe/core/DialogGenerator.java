package be.kdg.teame.kandoe.core;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;

import be.kdg.teame.kandoe.R;

public class DialogGenerator {

    public static void showErrorDialog(Context context, @StringRes int titleId, @StringRes int messageId){
        showErrorDialog(context, context.getString(titleId), context.getString(messageId));
    }

    public static void showErrorDialog(Context context, @StringRes int messageId){
        showErrorDialog(context, context.getString(R.string.dialog_error_title_default), context.getString(messageId));
    }

    public static void showErrorDialog(Context context, CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok, null);

        Dialog errorDialog = builder.create();
        errorDialog.show();
    }

    public static ProgressDialog createProgressDialog(Context context, @StringRes int messageId){
        ProgressDialog progressDialog = createProgressDialog(context);
        progressDialog.setMessage(context.getString(messageId));

        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        return progressDialog;
    }
}
