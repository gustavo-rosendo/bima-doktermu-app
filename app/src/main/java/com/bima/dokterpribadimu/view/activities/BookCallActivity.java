package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.BookingApi;
import com.bima.dokterpribadimu.databinding.ActivityBookCallBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TimeUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookCallActivity extends BaseActivity {

    private static final String SICKNESS_STRING = "Gejala Penyakit";
    private static final String HEALTHY_LIFE_STRING = "Gaya hidup sehat";
    private static final String TIPS_STRING = "Tips Kesehatan";
    private static final String SICKNESS = "sickness";
    private static final String HEALTHY_LIFE = "healthy_life";
    private static final String TIPS = "tips";

    private static final String TAG = BookCallActivity.class.getSimpleName();

    @Inject
    BookingApi bookingApi;

    private ActivityBookCallBinding binding;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_call);

        DokterPribadimuApplication.getComponent().inject(this);

        // Obtain the shared Tracker instance.
        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "Setting screen name: " + TAG);
        mTracker.setScreenName("Image~" + TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initViews() {
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        validateBookTimeLimit(); //Gray-out the button if a call was booked in less than the minimum interval

        binding.bookCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateTopic() && validateBookTimeLimit()) {
                    final String topicString = binding.subscriptionTopicSpinner.getSelectedItem().toString();
                    String topic;
                    if (topicString.equalsIgnoreCase(SICKNESS_STRING)) {
                        topic = SICKNESS;
                    } else if (topicString.equalsIgnoreCase(HEALTHY_LIFE_STRING)) {
                        topic = HEALTHY_LIFE;
                    } else {
                        topic = TIPS;
                    }
                    bookCall(topic, UserProfileUtils.getUserProfile(BookCallActivity.this).getAccessToken());
                }
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.call_about_arrays)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); // Hint to be displayed
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.subscriptionTopicSpinner.setAdapter(spinnerAdapter);
        binding.subscriptionTopicSpinner.setSelection(spinnerAdapter.getCount());
    }


    /**
     * Validate book a call topic
     * @return boolean true if topic are valid, boolean false if otherwise
     */
    private boolean validateTopic() {
        if (binding.subscriptionTopicSpinner.getSelectedItemPosition()
                == getResources().getStringArray(R.array.call_about_arrays).length - 1) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_call_topic_message),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            return true;
        }
        return false;
    }

    /**
     * Check if the minimum time between calls has already passed
     * @return boolean true if user can already book another call, boolean false if otherwise
     */
    private boolean validateBookTimeLimit() {
        double lastBookedCallTimeMillis = StorageUtils.getDouble(
                BookCallActivity.this,
                Constants.KEY_BOOK_CALL_TIME_MILLIS,
                -TimeUtils.ONE_HOUR_MS); //for the first time this is accessed

        if(!TimeUtils.hasOneHourPassed(lastBookedCallTimeMillis)) {
            binding.bookCallButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            Toast.makeText(
                    this,
                    getString(R.string.time_limit_to_book_call),
                    Toast.LENGTH_LONG
            ).show();
        } else {
            binding.bookCallButton.getBackground().setColorFilter(null);
            return true;
        }
        return false;
    }

    /**
     * Do book a call.
     * @param callTopic user's email
     * @param accessToken user's access token
     */
    private void bookCall(final String callTopic, final String accessToken) {
        bookingApi.bookCall(callTopic, accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        StorageUtils.putDouble(
                                BookCallActivity.this,
                                Constants.KEY_BOOK_CALL_TIME_MILLIS,
                                -TimeUtils.ONE_HOUR_MS); //ensure that the button will be active
                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        dismissProgressDialog();

                        if (response.getStatus() == Constants.Status.SUCCESS) {
                            StorageUtils.putDouble(
                                    BookCallActivity.this,
                                    Constants.KEY_BOOK_CALL_TIME_MILLIS,
                                    TimeUtils.getElapsedTimeMillis());

                            showSuccessDialog(
                                    R.drawable.ic_thumb_up,
                                    getString(R.string.dialog_book_call_success),
                                    getString(R.string.dialog_book_call_done_message),
                                    getString(R.string.dialog_take_me_home),
                                    new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                        @Override
                                        public void onClick(DokterPribadimuDialog dialog) {
                                            startDoctorCallActivityOnTop();
                                        }
                                    }
                            );
                        } else {
                            StorageUtils.putDouble(
                                    BookCallActivity.this,
                                    Constants.KEY_BOOK_CALL_TIME_MILLIS,
                                    -TimeUtils.ONE_HOUR_MS); //ensure that the button will be active
                            handleError(TAG, response.getMessage());
                        }
                    }
                });
    }
}
