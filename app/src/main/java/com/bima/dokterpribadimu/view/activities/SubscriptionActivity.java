package com.bima.dokterpribadimu.view.activities;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.ActivitySubscriptionBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.SubscriptionUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;

import java.util.Calendar;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubscriptionActivity extends BaseActivity {

    private static final String TAG = SubscriptionActivity.class.getSimpleName();

    private static final String DOB_FORMAT = "%d %s %d";

    private static final int MONTH_INT_JAN = 0;
    private static final int MONTH_INT_FEB = 1;
    private static final int MONTH_INT_MAR = 2;
    private static final int MONTH_INT_APR = 3;
    private static final int MONTH_INT_MAY = 4;
    private static final int MONTH_INT_JUN = 5;
    private static final int MONTH_INT_JUL = 6;
    private static final int MONTH_INT_AUG = 7;
    private static final int MONTH_INT_SEP = 8;
    private static final int MONTH_INT_OCT = 9;
    private static final int MONTH_INT_NOV = 10;
    private static final int MONTH_INT_DEC = 11;
    private static final String MONTH_STRING_JAN = "Januari";
    private static final String MONTH_STRING_FEB = "Februari";
    private static final String MONTH_STRING_MAR = "Maret";
    private static final String MONTH_STRING_APR = "April";
    private static final String MONTH_STRING_MAY = "Mei";
    private static final String MONTH_STRING_JUN = "Juni";
    private static final String MONTH_STRING_JUL = "Juli";
    private static final String MONTH_STRING_AUG = "Agustus";
    private static final String MONTH_STRING_SEP = "September";
    private static final String MONTH_STRING_OCT = "Oktober";
    private static final String MONTH_STRING_NOV = "Nopember";
    private static final String MONTH_STRING_DEC = "Desember";

    @Inject
    UserApi userApi;

    private ActivitySubscriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        initViews();
    }

    private void initViews() {
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.subscriptionSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSubscription()) {
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_ACTIVATE_SCREEN_SUBSCRIPTION);

                    UserProfile userProfile = UserProfileUtils.getUserProfile(SubscriptionActivity.this);
                    if (userProfile != null) {
                        //Prepare to call API /v1/user/update
                        userProfile.setDateOfBirth(
                                SubscriptionUtils
                                        .formatDateOfBirth(binding.subscriptionDobField.getText().toString())
                        );
                        String genderField = SubscriptionUtils.getChosenGender(
                                binding.subscriptionGenderSpinner.getSelectedItem().toString());
                        userProfile.setGender(genderField);
                        userProfile.setMsisdn(binding.subscriptionPhoneField.getText().toString());

                        //call API /v1/user/update and check for the result
                        updateUser(userProfile);

                    } else {
                        showErrorDialog(TAG + ".SubscribeButtonClickFailed",
                                R.drawable.ic_bug,
                                getString(R.string.dialog_failed),
                                getString(R.string.dialog_sign_in_failed_message),
                                getString(R.string.dialog_try_once_more),
                                null);
                    }
                }
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.gender_arrays)) {
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
        binding.subscriptionGenderSpinner.setAdapter(spinnerAdapter);
        binding.subscriptionGenderSpinner.setSelection(spinnerAdapter.getCount());
        binding.subscriptionDobField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_SUBSCRIPTION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Validate subscription fields.
     * @return boolean true if subscription data are valid, boolean false if otherwise
     */
    private boolean validateSubscription() {
        if (!ValidationUtils.isValidPhoneNumber(binding.subscriptionPhoneField.getText().toString())) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_phone_number_message),
                    Toast.LENGTH_SHORT
            ).show();
        } else if (binding.subscriptionGenderSpinner.getSelectedItemPosition()
                == getResources().getStringArray(R.array.gender_arrays).length - 1) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_gender_message),
                    Toast.LENGTH_SHORT
            ).show();
        } else if (TextUtils.isEmpty(binding.subscriptionDobField.getText().toString()) ||
                binding.subscriptionDobField.getText().toString().equalsIgnoreCase(getString(R.string.subscription_dob))) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_dob_message),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            return true;
        }
        return false;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.subscriptionDobField.setText(String.format(DOB_FORMAT, day, getMonthString(month), year));
                binding.subscriptionDobField.setTextColor(ContextCompat.getColor(SubscriptionActivity.this, android.R.color.primary_text_light));
            }
        }, year, month, day);
        dialog.show();
    }

    private String getMonthString(int month) {
        switch (month) {
            case MONTH_INT_JAN:
                return MONTH_STRING_JAN;
            case MONTH_INT_FEB:
                return MONTH_STRING_FEB;
            case MONTH_INT_MAR:
                return MONTH_STRING_MAR;
            case MONTH_INT_APR:
                return MONTH_STRING_APR;
            case MONTH_INT_MAY:
                return MONTH_STRING_MAY;
            case MONTH_INT_JUN:
                return MONTH_STRING_JUN;
            case MONTH_INT_JUL:
                return MONTH_STRING_JUL;
            case MONTH_INT_AUG:
                return MONTH_STRING_AUG;
            case MONTH_INT_SEP:
                return MONTH_STRING_SEP;
            case MONTH_INT_OCT:
                return MONTH_STRING_OCT;
            case MONTH_INT_NOV:
                return MONTH_STRING_NOV;
            case MONTH_INT_DEC:
                return MONTH_STRING_DEC;
            default:
                return MONTH_STRING_JAN;
        }
    }

    /**
     * Update user's data inputted in the subscription form.
     * @param userProfile user's data
     */
    private void updateUser(final UserProfile userProfile) {
        userApi.update(userProfile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(TAG + ".UpdateUserBeforeSubscription", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.getStatus() == Constants.Status.SUCCESS) {
                            //initPurchaseFlow();
                            IntentUtils.startSubscriptionPlansActivity(SubscriptionActivity.this);
                        } else {
                            handleError(TAG + ".UpdateUserBeforeSubscription", response.getMessage());
                        }
                    }
                });
    }

}
