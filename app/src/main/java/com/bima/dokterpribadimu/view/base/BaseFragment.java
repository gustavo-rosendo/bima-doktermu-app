package com.bima.dokterpribadimu.view.base;


import android.content.Intent;
import android.os.Bundle;

import com.bima.dokterpribadimu.view.activities.DoctorCallActivity;
import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.trello.rxlifecycle.components.support.RxFragment;

import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.*;

/**
 * Created by apradanas on 2/3/16.
 */
public class BaseFragment extends RxFragment {

    DokterPribadimuDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new DokterPribadimuDialog(getActivity());
    }

    @Override
    public void onDestroy() {
        dialog.setClickListener(null);
        dialog = null;

        super.onDestroy();
    }

    protected void showErrorDialog(
            String title, String message, String buttonText, OnDokterPribadimuDialogClickListener clickListener) {
        dialog.setDialogType(DIALOG_TYPE_ERROR)
                .setDialogCancelable(true)
                .setDialogTitle(title)
                .setDialogMessage(message)
                .setDialogButtonText(buttonText)
                .setClickListener(clickListener)
                .showDialog();
    }

    protected void showSuccessDialog(
            String title, String message, String buttonText, OnDokterPribadimuDialogClickListener clickListener) {
        dialog.setDialogType(DIALOG_TYPE_SUCCESS)
                .setDialogCancelable(false)
                .setDialogTitle(title)
                .setDialogMessage(message)
                .setDialogButtonText(buttonText)
                .setClickListener(clickListener)
                .showDialog();
    }

    protected void showLateDialog(String buttonText, OnDokterPribadimuDialogClickListener clickListener) {
        dialog.setDialogType(DIALOG_TYPE_LATE)
                .setDialogCancelable(false)
                .setDialogTitle(null)
                .setDialogMessage(null)
                .setDialogButtonText(buttonText)
                .setClickListener(clickListener)
                .showDialog();
    }

    protected void startHomeActivity() {
        startActivity(new Intent(getActivity(), DoctorCallActivity.class));
    }

    protected void startDoctorCallActivityOnTop() {
        Intent intent = new Intent(getActivity(), DoctorCallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void startLandingActivityOnTop() {
        Intent intent = new Intent(getActivity(), LandingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
