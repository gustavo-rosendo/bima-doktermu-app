package com.bima.dokterpribadimu.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.BookingApi;
import com.bima.dokterpribadimu.databinding.ActivityBookCallBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.utils.BookingUtils;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TimeUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.bima.dokterpribadimu.view.components.PhoneInfoModalDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookCallActivity extends BaseActivity {

    private static final String TAG = BookCallActivity.class.getSimpleName();

    private static final String CALL_ID = "call_id";

    @Inject
    BookingApi bookingApi;

    private ActivityBookCallBinding binding;

    private Tracker mTracker;

    private PhoneInfoModalDialog phoneInfoModalDialog;

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
                if (validateTopic() && validateSubTopic() && validatePhoneNumber() && validateBookTimeLimit()) {
                    String topic = "";
                    String subTopic = "";

                    final int topicPosition = binding.bookCallTopicSpinner.getSelectedItemPosition();
                    int subTopicPosition = -1;

                    switch (topicPosition) {
                        case 0:
                            topic = BookingUtils.TOPIC_REPRODUCTION;

                            subTopicPosition = binding.bookCallSubtopicSpinner.getSelectedItemPosition();
                            switch (subTopicPosition) {
                                case 0:
                                    subTopic = BookingUtils.SUBTOPIC_PREGNANCY;
                                    break;
                                case 1:
                                    subTopic = BookingUtils.SUBTOPIC_BABY_CHILDREN_HEALTH;
                                    break;
                                case 2:
                                    subTopic = BookingUtils.SUBTOPIC_HEALTH_SEXUAL_DISEASE;
                                    break;
                                default:
                                    subTopic = "";
                                    break;
                            }

                            break;
                        case 1:
                            topic = BookingUtils.TOPIC_HEALTH_ORGAN_DISEASE;

                            subTopicPosition = binding.bookCallSubtopicSpinner.getSelectedItemPosition();
                            switch (subTopicPosition) {
                                case 0:
                                    subTopic = BookingUtils.SUBTOPIC_INTERNAL_DISEASE;
                                    break;
                                case 1:
                                    subTopic = BookingUtils.SUBTOPIC_BONE_DISEASE;
                                    break;
                                case 2:
                                    subTopic = BookingUtils.SUBTOPIC_NEURAL_DISEASE;
                                    break;
                                case 3:
                                    subTopic = BookingUtils.SUBTOPIC_EYE_DISEASE;
                                    break;
                                case 4:
                                    subTopic = BookingUtils.SUBTOPIC_THT_DISEASE;
                                    break;
                                default:
                                    subTopic = "";
                                    break;
                            }

                            break;
                        case 2:
                            topic = BookingUtils.TOPIC_DIET_NUTRITION;
                            subTopic = "";
                            break;
                        case 3:
                            topic = BookingUtils.TOPIC_INJURY;
                            subTopic = "";
                            break;
                        case 4:
                            topic = BookingUtils.TOPIC_OTHERS;
                            subTopic = "";
                            break;
                        default:
                            topic = "";
                            subTopic = "";
                            break;
                    }

                    bookCall(topic, subTopic,
                            UserProfileUtils.getUserProfile(BookCallActivity.this).getMsisdn(),
                            UserProfileUtils.getUserProfile(BookCallActivity.this).getAccessToken());
                }
            }
        });

        final ArrayAdapter<String> spinnerSubTopicReproductionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.call_reproduksi_subtopics_arrays)) {
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
        spinnerSubTopicReproductionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> spinnerSubTopicHealthAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.call_kesehatan_subtopics_arrays)) {
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
        spinnerSubTopicHealthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> spinnerTopicsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.call_topics_arrays)) {
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
        spinnerTopicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.bookCallTopicSpinner.setAdapter(spinnerTopicsAdapter);
        binding.bookCallTopicSpinner.setSelection(spinnerTopicsAdapter.getCount());

        binding.bookCallTopicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        binding.bookCallSubtopicSpinner.setAdapter(spinnerSubTopicReproductionAdapter);
                        binding.bookCallSubtopicSpinner.setSelection(spinnerSubTopicReproductionAdapter.getCount());
                        binding.bookCallSubtopicUnderlineView.setVisibility(View.VISIBLE);
                        binding.bookCallSubtopicSpinner.setVisibility(View.VISIBLE);
                        binding.bookCallSubtopicSpinner.setEnabled(true);
                        break;
                    case 1:
                        binding.bookCallSubtopicSpinner.setAdapter(spinnerSubTopicHealthAdapter);
                        binding.bookCallSubtopicSpinner.setSelection(spinnerSubTopicHealthAdapter.getCount());
                        binding.bookCallSubtopicUnderlineView.setVisibility(View.VISIBLE);
                        binding.bookCallSubtopicSpinner.setVisibility(View.VISIBLE);
                        binding.bookCallSubtopicSpinner.setEnabled(true);
                        break;
                    default:
                        binding.bookCallSubtopicUnderlineView.setVisibility(View.GONE);
                        binding.bookCallSubtopicSpinner.setVisibility(View.GONE);
                        binding.bookCallSubtopicSpinner.setEnabled(false);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //In the beginning, the subtopics list is invisible
        binding.bookCallSubtopicUnderlineView.setVisibility(View.GONE);
        binding.bookCallSubtopicSpinner.setVisibility(View.GONE);
        binding.bookCallSubtopicSpinner.setEnabled(false);

        //Show the phone number where the user will be called
        binding.bookCallPhoneNumber.setText(UserProfileUtils.getUserProfile(BookCallActivity.this).getMsisdn());

        binding.bookCallPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneInfoModalDialog == null) {
                    phoneInfoModalDialog = new PhoneInfoModalDialog(BookCallActivity.this);
                }

                phoneInfoModalDialog.setListener(new PhoneInfoModalDialog.OnPhoneInfoModalDialogClickListener() {
                    @Override
                    public void onClick(PhoneInfoModalDialog dialog) {
                        dismissPhoneInfoModalDialog();
                    }
                });

                phoneInfoModalDialog.showDialog();
            }
        });

        binding.bookCallPhoneInfoModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneInfoModalDialog == null) {
                    phoneInfoModalDialog = new PhoneInfoModalDialog(BookCallActivity.this);
                }

                phoneInfoModalDialog.setListener(new PhoneInfoModalDialog.OnPhoneInfoModalDialogClickListener() {
                    @Override
                    public void onClick(PhoneInfoModalDialog dialog) {
                        dismissPhoneInfoModalDialog();
                    }
                });

                phoneInfoModalDialog.showDialog();
            }
        });

        binding.bookCallEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getResources().getString(R.string.book_call_email);
                startMailIntent(email);
            }
        });
    }

    private void startMailIntent(String email) {
        Intent emailIntent = new Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", email, null));
        startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    private void dismissPhoneInfoModalDialog() {
        phoneInfoModalDialog.dismiss();
        phoneInfoModalDialog.clearReference();
        phoneInfoModalDialog = null;
    }

    /**
     * Validate book a call topic
     * @return boolean true if topic are valid, boolean false if otherwise
     */
    private boolean validateTopic() {
        if (binding.bookCallTopicSpinner.getSelectedItemPosition()
                == getResources().getStringArray(R.array.call_topics_arrays).length - 1) {
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
     * Validate book a call sub-topic
     * @return boolean true if sub-topic are valid, boolean false if otherwise
     */
    private boolean validateSubTopic() {
        boolean isValid = false;

        if(binding.bookCallSubtopicSpinner.getVisibility() == View.VISIBLE) {
            if (binding.bookCallSubtopicSpinner.getSelectedItemPosition()
                    == binding.bookCallSubtopicSpinner.getCount()) {
                Toast.makeText(
                        this,
                        getString(R.string.invalid_call_subtopic_message),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                return true;
            }
        }
        else {
            isValid = true;
        }

        return isValid;
    }

    /**
     * Validate book a call phone number. A phone number is valid if it has at least 11 digits, like in: 02742112462
     * @return boolean true if phone number is valid, boolean false if otherwise
     */
    private boolean validatePhoneNumber() {
        boolean isValid = false;

        if(binding.bookCallPhoneNumber.getText().length() < Constants.PHONE_NUMBER_MINIMAL_DIGITS) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_call_phone_number_message),
                    Toast.LENGTH_LONG
            ).show();
        }
        else {
            isValid = true;
        }

        return isValid;
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
    private void bookCall(final String callTopic, final String callSubTopic,
                          final String phoneNumber, final String accessToken) {
        bookingApi.bookCall(callTopic, callSubTopic, phoneNumber, accessToken)
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

                            if(response.getData() != null) {
                                String jsonResponse = response.getData().toString();
                                String callId = "";
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonResponse);
                                    if(jsonObject.has(CALL_ID)) {
                                        callId = String.valueOf(jsonObject.optInt(CALL_ID));
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "Error reading call_id from jsonResponse = " + jsonResponse);
                                    Log.e(TAG, "Error message = " + e.getMessage());
                                }
                                StorageUtils.putString(
                                        BookCallActivity.this,
                                        Constants.KEY_BOOK_CALL_ID_LAST_CALL,
                                        callId);
                                Log.d(TAG, "Storing last call's id = " + callId);
                            }

                            showSuccessDialog(
                                    R.drawable.ic_thumb_up,
                                    getString(R.string.dialog_book_call_success),
                                    getString(R.string.dialog_book_call_done_message),
                                    getString(R.string.dialog_take_me_home),
                                    new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                        @Override
                                        public void onClick(DokterPribadimuDialog dialog) {
                                            IntentUtils.startDoctorCallActivityOnTop(BookCallActivity.this);
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
