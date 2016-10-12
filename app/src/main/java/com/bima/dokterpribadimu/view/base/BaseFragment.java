package com.bima.dokterpribadimu.view.base;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.view.activities.AboutActivity;
import com.bima.dokterpribadimu.view.activities.BookCallActivity;
import com.bima.dokterpribadimu.view.activities.DoctorCallActivity;
import com.bima.dokterpribadimu.view.activities.DoctorCallPendingActivity;
import com.bima.dokterpribadimu.view.activities.DoctorCallAssignedActivity;
import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.activities.ProfileActivity;
import com.bima.dokterpribadimu.view.activities.SubscriptionActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.bima.dokterpribadimu.view.components.DokterPribadimuProgressDialog;
import com.trello.rxlifecycle.components.support.RxFragment;

import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_ERROR;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_LATE;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.DIALOG_TYPE_SUCCESS;
import static com.bima.dokterpribadimu.view.components.DokterPribadimuDialog.OnDokterPribadimuDialogClickListener;

/**
 * Created by apradanas on 2/3/16.
 */
public class BaseFragment extends RxFragment {

    DokterPribadimuDialog dialog;
    DokterPribadimuProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new DokterPribadimuDialog(getActivity());
        progressDialog = new DokterPribadimuProgressDialog(getActivity());
    }

    @Override
    public void onDestroy() {
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
            Toast.makeText(getActivity(), getString(R.string.no_connection_message), Toast.LENGTH_SHORT).show();
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

        AnalyticsHelper.logViewDialogFailedEvent(
                EventConstants.DIALOG_GENERAL_STATUS_FAILED,
                tag + " - " + message);
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

    protected void showLateDialog(String buttonText, OnDokterPribadimuDialogClickListener clickListener) {
        dialog.setDialogType(DIALOG_TYPE_LATE)
                .setDialogCancelable(false)
                .setDialogTitle(null)
                .setDialogMessage(null)
                .setDialogButtonText(buttonText)
                .setClickListener(clickListener)
                .showDialog();

        AnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_DOCTOR_CALL_LATE_HOURS);
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

    protected void startDoctorCallActivity() {
        startActivity(new Intent(getActivity(), DoctorCallActivity.class));
    }

    protected void startDoctorCallActivityOnTop() {
        Intent intent = new Intent(getActivity(), DoctorCallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void startDoctorCallPendingActivity() {
        startActivity(new Intent(getActivity(), DoctorCallPendingActivity.class));
    }

    protected void startDoctorCallActivityPendingOnTop() {
        Intent intent = new Intent(getActivity(), DoctorCallPendingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void startDoctorCallAssignedActivity() {
        startActivity(new Intent(getActivity(), DoctorCallAssignedActivity.class));
    }

    protected void startDoctorCallAssignedActivityOnTop() {
        Intent intent = new Intent(getActivity(), DoctorCallAssignedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void startSubscriptionActivity() {
        startActivity(new Intent(getActivity(), SubscriptionActivity.class));
    }

    protected void startBookCallActivity() {
        startActivity(new Intent(getActivity(), BookCallActivity.class));
    }

    protected void startAboutActivity() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    protected void startProfileActivity() {
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    protected void startLandingActivityOnTop() {
        Intent intent = new Intent(getActivity(), LandingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void startViewIntent(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    protected void startDialIntent(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    protected void startFacebookIntent() {
        String url = Constants.BIMA_FACEBOOK_PAGE;
        try {
            PackageInfo info = getActivity()
                                .getPackageManager()
                                .getPackageInfo(Constants.FACEBOOK_APP_ID, 0);
            if (info.applicationInfo.enabled) {
                url = "fb://facewebmodal/f?href=" + url;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        startViewIntent(url);
    }

    protected void startTwitterIntent() {
        String url = Constants.BIMA_TWITTER;
        try {
            PackageInfo info = getActivity()
                                    .getPackageManager()
                                    .getPackageInfo(Constants.TWITTER_APP_ID, 0);
            if (info.applicationInfo.enabled) {
                url = "twitter://user?screen_name=bima_ind";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        startViewIntent(url);
    }

    protected void startMailIntent() {
        Intent emailIntent = new Intent(
                                    Intent.ACTION_SENDTO,
                                    Uri.fromParts("mailto", Constants.BIMA_EMAIL, null));
        startActivity(Intent.createChooser(emailIntent, "Send email"));
    }
}
