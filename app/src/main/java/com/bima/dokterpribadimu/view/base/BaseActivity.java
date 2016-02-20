package com.bima.dokterpribadimu.view.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.facebook.appevents.AppEventsLogger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by apradanas on 2/3/16.
 */
public class BaseActivity extends RxAppCompatActivity {

    DokterPribadimuDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dialog = new DokterPribadimuDialog(this);
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

        super.onDestroy();
    }

    protected void showErrorDialog(String title, String message) {
        dialog.setDialogType(DokterPribadimuDialog.DIALOG_TYPE_ERROR)
                .setDialogTitle(title)
                .setDialogMessage(message)
                .showDialog();
    }

    protected void showSuccessDialog(String title, String message) {
        dialog.setDialogType(DokterPribadimuDialog.DIALOG_TYPE_SUCCESS)
                .setDialogTitle(title)
                .setDialogMessage(message)
                .showDialog();
    }

    protected void showLateDialog() {
        dialog.setDialogType(DokterPribadimuDialog.DIALOG_TYPE_LATE)
                .setDialogTitle(null)
                .setDialogMessage(null)
                .showDialog();
    }

    protected void startLandingActivity() {
        startActivity(new Intent(this, LandingActivity.class));
    }

    protected void startSignInActivity() {
        startActivity(new Intent(this, SignInActivity.class));
    }
}
