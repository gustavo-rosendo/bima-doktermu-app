package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.google.android.gms.common.server.converter.StringToIntConverter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookCallActivity extends BaseActivity {

    private static final String TOPIC_REPRODUCTION = "reproduksi";
    private static final String TOPIC_HEALTH_ORGAN_DISEASE = "kesehatan_penyakit_organ";
    private static final String TOPIC_DIET_NUTRITION = "diet_nutrisi";
    private static final String TOPIC_INJURY = "cedera";
    private static final String TOPIC_OTHERS = "lain_lain";

    private static final String SUBTOPIC_PREGNANCY = "kehamilan_kesuburan";
    private static final String SUBTOPIC_BABY_CHILDREN_HEALTH = "kesehatan_bayi_anak";
    private static final String SUBTOPIC_HEALTH_SEXUAL_DISEASE = "kesehatan_penyakit_kelamin";

    private static final String SUBTOPIC_INTERNAL_DISEASE = "penyakit_dalam";
    private static final String SUBTOPIC_BONE_DISEASE = "penyakit_tulang";
    private static final String SUBTOPIC_NEURAL_DISEASE = "penyakit_syaraf";
    private static final String SUBTOPIC_EYE_DISEASE = "penyakit_mata";
    private static final String SUBTOPIC_THT_DISEASE = "penyakit_tht";

    private static final String TAG = BookCallActivity.class.getSimpleName();

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
                if (validateTopic() && validateBookTimeLimit()) {
                    String topic = "";
                    String subTopic = "";

                    final int topicPosition = binding.bookCallTopicSpinner.getSelectedItemPosition();
                    int subTopicPosition = -1;

                    switch (topicPosition) {
                        case 0:
                            topic = TOPIC_REPRODUCTION;

                            subTopicPosition = binding.bookCallSubtopicSpinner.getSelectedItemPosition();
                            switch (subTopicPosition) {
                                case 0:
                                    subTopic = SUBTOPIC_PREGNANCY;
                                    break;
                                case 1:
                                    subTopic = SUBTOPIC_BABY_CHILDREN_HEALTH;
                                    break;
                                case 2:
                                    subTopic = SUBTOPIC_HEALTH_SEXUAL_DISEASE;
                                    break;
                                default:
                                    subTopic = "";
                                    break;
                            }

                            break;
                        case 1:
                            topic = TOPIC_HEALTH_ORGAN_DISEASE;

                            subTopicPosition = binding.bookCallSubtopicSpinner.getSelectedItemPosition();
                            switch (subTopicPosition) {
                                case 0:
                                    subTopic = SUBTOPIC_INTERNAL_DISEASE;
                                    break;
                                case 1:
                                    subTopic = SUBTOPIC_BONE_DISEASE;
                                    break;
                                case 2:
                                    subTopic = SUBTOPIC_NEURAL_DISEASE;
                                    break;
                                case 3:
                                    subTopic = SUBTOPIC_EYE_DISEASE;
                                    break;
                                case 4:
                                    subTopic = SUBTOPIC_THT_DISEASE;
                                    break;
                                default:
                                    subTopic = "";
                                    break;
                            }

                            break;
                        case 2:
                            topic = TOPIC_DIET_NUTRITION;
                            subTopic = "";
                            break;
                        case 3:
                            topic = TOPIC_INJURY;
                            subTopic = "";
                            break;
                        case 4:
                            topic = TOPIC_OTHERS;
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
                        binding.bookCallSubtopicUnderlineView.setVisibility(View.INVISIBLE);
                        binding.bookCallSubtopicSpinner.setVisibility(View.INVISIBLE);
                        binding.bookCallSubtopicSpinner.setEnabled(false);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //In the beginning, the subtopics list is invisible
        binding.bookCallSubtopicUnderlineView.setVisibility(View.INVISIBLE);
        binding.bookCallSubtopicSpinner.setVisibility(View.INVISIBLE);
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
