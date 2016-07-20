package com.bima.dokterpribadimu.view.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.analytics.FirebaseAnalyticsHelper;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.bima.dokterpribadimu.view.components.DokterPribadimuProgressDialog;
import com.facebook.appevents.AppEventsLogger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_ERROR;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_LATE;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_SUCCESS;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.OnDokterPribadimuDialogClickListener;

/**
 * Created by apradanas on 2/3/16.
 */
public class BaseActivity extends RxAppCompatActivity {

    DokterPribadimuDialog dialog;
    DokterPribadimuProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dialog = new DokterPribadimuDialog(this);
        progressDialog = new DokterPribadimuProgressDialog(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        dialog.setClickListener(null);
        dialog = null;

        progressDialog = null;

        super.onDestroy();
    }

    protected void handleError(String tag, String errorMessage) {
        if (errorMessage == null || errorMessage.contains(Constants.ILLEGAL_STATE_EXCEPTION)) {
            errorMessage = getString(R.string.dialog_sign_in_failed_message);
        }

        Log.e(tag, errorMessage);

        if (errorMessage.contains(Constants.NETWORK_IS_UNREACHABLE)
                || errorMessage.contains(Constants.UNABLE_TO_RESOLVE_HOST)) {
            Toast.makeText(this, getString(R.string.no_connection_message), Toast.LENGTH_SHORT).show();
        } else {
            showErrorDialog(tag,
                    R.drawable.ic_bug,
                    getString(R.string.dialog_failed),
                    errorMessage,
                    getString(R.string.dialog_try_once_more),
                    null);
        }
    }

    protected void showErrorDialog(String tag,
            int imageResource, String title, String message,
            String buttonText, OnDokterPribadimuDialogClickListener clickListener) {
        dialog.setDialogType(DIALOG_TYPE_ERROR)
                .setDialogImageResource(imageResource)
                .setDialogCancelable(true)
                .setDialogTitle(title)
                .setDialogMessage(message)
                .setDialogButtonText(buttonText)
                .setClickListener(clickListener)
                .showDialog();

        String errorMessage = message;
        if(errorMessage.length() > 35) {
            //avoid Firebase error due to limit of 36 characters
            errorMessage = message.substring(0, 34);
        }
        FirebaseAnalyticsHelper.logViewDialogFailedEvent(
                EventConstants.DIALOG_GENERAL_STATUS_FAILED,
                tag + " - " + errorMessage);
    }

    protected void showSuccessDialog(
            int imageResource, String title, String message,
            String buttonText, OnDokterPribadimuDialogClickListener clickListener) {
        dialog.setDialogType(DIALOG_TYPE_SUCCESS)
                .setDialogImageResource(imageResource)
                .setDialogCancelable(false)
                .setDialogTitle(title)
                .setDialogMessage(message)
                .setDialogButtonText(buttonText)
                .setClickListener(clickListener)
                .showDialog();
    }

    protected void showLateDialog(int imageResource, String buttonText, OnDokterPribadimuDialogClickListener clickListener) {
        dialog.setDialogType(DIALOG_TYPE_LATE)
                .setDialogImageResource(imageResource)
                .setDialogCancelable(false)
                .setDialogTitle(null)
                .setDialogMessage(null)
                .setDialogButtonText(buttonText)
                .setClickListener(clickListener)
                .showDialog();
    }

    protected void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
