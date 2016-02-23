package com.bima.dokterpribadimu.view.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.bima.dokterpribadimu.view.activities.DoctorCallActivity;
import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.facebook.appevents.AppEventsLogger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.*;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_ERROR;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_LATE;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_SUCCESS;

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

    protected void showErrorDialog(
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

    protected void startLandingActivity() {
        startActivity(new Intent(this, LandingActivity.class));
    }

    protected void startSignInActivity() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    protected void startDoctorCallActivityOnTop() {
        Intent intent = new Intent(this, DoctorCallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
