package com.bima.dokterpribadimu.view.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.bima.dokterpribadimu.data.inappbilling.BillingClient;
import com.bima.dokterpribadimu.data.inappbilling.BillingInitializationListener;
import com.bima.dokterpribadimu.data.inappbilling.QueryInventoryListener;
import com.bima.dokterpribadimu.data.remote.api.SubscriptionApi;
import com.bima.dokterpribadimu.databinding.ActivitySubscriptionBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Subscription;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TimeUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.utils.iabutil.IabHelper;
import com.bima.dokterpribadimu.utils.iabutil.IabResult;
import com.bima.dokterpribadimu.utils.iabutil.Purchase;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubscriptionActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = SubscriptionActivity.class.getSimpleName();

    private static final int RC_LOCATION_PERMISSION = 1;
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
    BillingClient billingClient;

    @Inject
    SubscriptionApi subscriptionApi;

    private ActivitySubscriptionBinding binding;

    private Location location;
    private LocationTracker locationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        initLocation();
        initViews();
    }

    private void initBillingClient() {
        billingClient.setBillingInitializationListener(new BillingInitializationListener() {
            @Override
            public void onSuccess() {
                billingClient.queryInventoryAsync();
            }

            @Override
            public void onFailed() {
                // TODO: handle this
            }
        });

        billingClient.setQueryInventoryListener(new QueryInventoryListener() {
            @Override
            public void onSuccess(boolean isSubscribed) {
                StorageUtils.putBoolean(
                        SubscriptionActivity.this,
                        Constants.KEY_USER_SUBSCIPTION,
                        isSubscribed);
            }

            @Override
            public void onFailed() {
                // TODO: handle this
            }
        });

        billingClient.init(this);
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
                    if (!billingClient.isSubscribedToDokterPribadiKu()) {
                        billingClient.launchSubscriptionPurchaseFlow(new IabHelper.OnIabPurchaseFinishedListener() {
                            @Override
                            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                                if (result.isSuccess()) {
                                    Subscription subscription = new Subscription();
                                    subscription.setAccessToken(
                                            UserProfileUtils.getUserProfile(SubscriptionActivity.this).getAccessToken());
                                    subscription.setSubscriptionType(Constants.SUBSCRIPTION_TYPE_NEW);
                                    subscription.setSubscriptionToken(info.getToken());
                                    subscription.setName(binding.subscriptionNameField.getText().toString());
                                    subscription.setOrderDate(TimeUtils.getSubscriptionOrderDate());
                                    subscription.setSubscriptionStart(TimeUtils.getSubscriptionStartDate());
                                    subscription.setSubscriptionEnd(TimeUtils.getSubscriptionEndDate());
                                    subscription.setOrderId(info.getOrderId());
                                    subscription.setPhoneNumber(binding.subscriptionPhoneField.getText().toString());
                                    subscription.setProductName(billingClient.getProductName(info.getSku()));
                                    subscription.setPrice(billingClient.getProductPrice(info.getSku()));
                                    subscription.setDateOfBirth(binding.subscriptionDobField.getText().toString());

                                    String genderField = binding.subscriptionGenderSpinner.toString();
                                    if (genderField != null) {
                                        if (genderField.contains("Laki") || genderField.contains("laki") || genderField.contains("Male")) {
                                            genderField = "Male";
                                        } else if (genderField.contains("Perempuan") || genderField.contains("perempuan") || genderField.contains("Female")) {
                                            genderField = "Female";
                                        } else {
                                            genderField = ""; //don't save anything, better than saving the wrong gender :P
                                        }
                                    }
                                    subscription.setGender(genderField);

                                    subscription.setDateOfPurchase(subscription.getOrderDate());
                                    subscription.setPolicyActiveDate(subscription.getSubscriptionStart());
                                    subscription.setPolicyExpiryDate(subscription.getSubscriptionEnd());

                                    if (location != null) {
                                        subscription.setSubscriptionLat(location.getLatitude());
                                        subscription.setSubscriptionLong(location.getLongitude());
                                    } else {
                                        subscription.setSubscriptionLat(0.0);
                                        subscription.setSubscriptionLong(0.0);
                                    }

                                    registerSubscription(subscription);
                                } else {
                                    showErrorDialog(
                                            R.drawable.ic_bug,
                                            getString(R.string.dialog_failed),
                                            getString(R.string.dialog_sign_in_failed_message),
                                            getString(R.string.dialog_try_once_more),
                                            null);
                                }
                            }
                        });
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

        UserProfile userProfile = UserProfileUtils.getUserProfile(this);
        if (userProfile != null) {
            binding.subscriptionNameField.setText(userProfile.getName());
            binding.subscriptionEmailField.setText(userProfile.getEmail());
        }

        binding.subscriptionDobField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    public void initLocation() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationTracker();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RC_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationTracker() {
        TrackerSettings settings =
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true);

        try {
            locationTracker = new LocationTracker(this, settings) {

                @Override
                public void onLocationFound(@NonNull Location location) {
                    SubscriptionActivity.this.location = location;
                    stopListening();
                }

                @Override
                public void onTimeout() {
                    startLocationTracker();
                }
            };
            locationTracker.startListening();
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        initBillingClient();
    }

    @Override
    protected void onDestroy() {
        billingClient.release();

        if (locationTracker != null) {
            locationTracker.stopListening();
        }

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingClient.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Validate subscription fields.
     * @return boolean true if subscription data are valid, boolean false if otherwise
     */
    private boolean validateSubscription() {
        if (!ValidationUtils.isValidName(binding.subscriptionNameField.getText().toString())) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_name_message),
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
        } else if (!ValidationUtils.isValidPhoneNumber(binding.subscriptionPhoneField.getText().toString())) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_phone_number_message),
                    Toast.LENGTH_SHORT
            ).show();
        } else if (!ValidationUtils.isValidEmail(binding.subscriptionEmailField.getText().toString())) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_email_message),
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
     * Do user login.
     * @param subscription user's subscription data
     */
    private void registerSubscription(final Subscription subscription) {
        subscriptionApi.registerSubscription(subscription)
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
                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.getStatus() == Constants.Status.SUCCESS) {
                            showSuccessDialog(
                                    R.drawable.ic_dialog_success,
                                    getString(R.string.dialog_success),
                                    getString(R.string.dialog_subscription_success_message),
                                    getString(R.string.dialog_book_first_call),
                                    new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                        @Override
                                        public void onClick(DokterPribadimuDialog dialog) {
                                            startBookCallActivity();
                                            finish();
                                        }
                                    }
                            );
                        } else {
                            handleError(TAG, response.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
