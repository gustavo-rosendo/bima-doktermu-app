package com.bima.dokterpribadimu.view.activities;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.data.inappbilling.BillingClient;
import com.bima.dokterpribadimu.data.inappbilling.BillingInitializationListener;
import com.bima.dokterpribadimu.data.inappbilling.QueryInventoryListener;
import com.bima.dokterpribadimu.data.remote.api.SubscriptionApi;
import com.bima.dokterpribadimu.databinding.ActivitySubscriptionPlansBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Subscription;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.SubscriptionUtils;
import com.bima.dokterpribadimu.utils.TimeUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.utils.iabutil.IabHelper;
import com.bima.dokterpribadimu.utils.iabutil.IabResult;
import com.bima.dokterpribadimu.utils.iabutil.Purchase;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;

import java.util.List;

import javax.inject.Inject;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gustavo.santos on 8/31/2016.
 */
public class SubscriptionPlansActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = SubscriptionPlansActivity.class.getSimpleName();

    private static final int RC_LOCATION_PERMISSION = 1;

    private ActivitySubscriptionPlansBinding binding;

    private static boolean isRegisterSubscriptionDone = false;

    @Inject
    BillingClient billingClient;

    private Subscription subscription;

    @Inject
    SubscriptionApi subscriptionApi;

    private Location location;
    private LocationTracker locationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_plans);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    void init() {
        isRegisterSubscriptionDone = false; //reset the flag
        initLocation();
        initViews();
    }

    private void initViews() {
        binding.subscriptionPlan1MonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPurchaseFlow(BillingClient.SKU_DOKTER_PRIBADIKU_1_MONTH_SUBSCRIPTION);
            }
        });

        binding.subscriptionPlan3MonthsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPurchaseFlow(BillingClient.SKU_DOKTER_PRIBADIKU_3_MONTHS_SUBSCRIPTION);
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
                    SubscriptionPlansActivity.this.location = location;
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
                        SubscriptionPlansActivity.this,
                        Constants.KEY_USER_SUBSCIPTION,
                        isSubscribed);

                //GUS - updateSubscription must be called ONLY once (after registerSubscription) in this class
                //      to ensure that the Email template "successful subscription" will be sent only once.
                if (isSubscribed && isRegisterSubscriptionDone) {
                    if (subscription == null) {
                        subscription = new Subscription();
                        String orderId = StorageUtils.getString(
                                SubscriptionPlansActivity.this,
                                Constants.KEY_USER_SUBSCRIPTION_ORDER_ID,
                                "");
                        subscription.setOrderId(orderId);
                    }

                    // Get the SKU of the previously purchased subscription
                    String subscriptionSKU = StorageUtils.getString(
                            SubscriptionPlansActivity.this,
                            Constants.KEY_USER_SUBSCRIPTION_SKU,
                            null);
                    subscription.setProductName(billingClient.getProductName(subscriptionSKU));
                    String priceStr = billingClient.getProductPrice(subscriptionSKU);
                    subscription.setPrice(SubscriptionUtils.formatSubscriptionPrice(priceStr));
                    subscription.setAccessToken(
                            UserProfileUtils.getUserProfile(SubscriptionPlansActivity.this).getAccessToken());

                    //call the API to update the product name and the price
                    updateSubscription(subscription);
                }
            }

            @Override
            public void onFailed() {
                // TODO: handle this
                showErrorDialog(TAG + ".QueryIabInventoryFailed",
                        R.drawable.ic_bug,
                        getString(R.string.dialog_failed),
                        getString(R.string.dialog_sign_in_failed_message),
                        getString(R.string.dialog_try_once_more),
                        null);
            }
        });

        billingClient.init(this);
    }

    private void initPurchaseFlow(final String sku) {
        if (!billingClient.isSubscribedToDokterPribadiKu()) {
            billingClient.launchSubscriptionPurchaseFlow(sku, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.isSuccess()) {
                        //save the SKU for later use
                        StorageUtils.putString(
                                SubscriptionPlansActivity.this,
                                Constants.KEY_USER_SUBSCRIPTION_SKU,
                                sku);

                        subscription = new Subscription();
                        subscription.setSubscriptionType(Constants.SUBSCRIPTION_TYPE_NEW);

                        if (location != null) {
                            subscription.setSubscriptionLat(location.getLatitude());
                            subscription.setSubscriptionLong(location.getLongitude());
                        } else {
                            subscription.setSubscriptionLat(0.0);
                            subscription.setSubscriptionLong(0.0);
                        }

                        subscription.setSubscriptionToken(info.getToken());
                        subscription.setSubscriptionStart(TimeUtils.getSubscriptionStartDate());
                        subscription.setSubscriptionEnd(TimeUtils.getSubscriptionEndDate(sku));
                        subscription.setOrderDate(TimeUtils.getSubscriptionOrderDate());
                        subscription.setOrderId(info.getOrderId());
                        //save the orderId for later use by /v1/subscription/update
                        StorageUtils.putString(
                                SubscriptionPlansActivity.this,
                                Constants.KEY_USER_SUBSCRIPTION_ORDER_ID,
                                subscription.getOrderId());
                        subscription.setPaymentMethod(""); //for now it's not sent by Google but maybe in the future it will
                        subscription.setProductName(billingClient.getProductName(info.getSku()));
                        subscription.setPrice(billingClient.getProductPrice(info.getSku()));
                        subscription.setDateOfPurchase(subscription.getOrderDate());
                        subscription.setPolicyActiveDate(subscription.getSubscriptionStart());
                        subscription.setPolicyExpiryDate(subscription.getSubscriptionEnd());

                        if(sku.equalsIgnoreCase(BillingClient.SKU_DOKTER_PRIBADIKU_3_MONTHS_SUBSCRIPTION)) {
                            subscription.setPolicy(SubscriptionUtils.POLICY_3_MONTHS);
                        }
                        else if(sku.equalsIgnoreCase(BillingClient.SKU_DOKTER_PRIBADIKU_1_MONTH_SUBSCRIPTION)) {
                            subscription.setPolicy(SubscriptionUtils.POLICY_1_MONTH);
                        }
                        else {
                            subscription.setPolicy(SubscriptionUtils.POLICY_1_MONTH_OLD);
                        }

                        subscription.setAccessToken(
                                UserProfileUtils.getUserProfile(SubscriptionPlansActivity.this).getAccessToken());

                        //Doktermu Tracking - Subscription
                        //Google AdWords Android in-app conversion tracking snippet for successful Subscription
                        AnalyticsHelper.reportAdWordsConversionSubscription();

                        AnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_SUBSCRIPTION_GOOGLEPLAY_SUCCESS);

                        registerSubscription(subscription);

                    } else {
                        showErrorDialog(TAG + ".GooglePurchaseFailedOrCanceled",
                                R.drawable.ic_bug,
                                getString(R.string.dialog_failed),
                                getString(R.string.dialog_google_play_failed_message)
                                        + result.getMessage(), //GUS - show Google's message to the user because it's more specific
                                getString(R.string.dialog_try_once_more),
                                null);
                    }
                }
            });
        }
    }

    /**
     * Do subscription registration after Google's successful response.
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
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        handleError(TAG + ".RegisterSubscriptionAfterPayment", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        dismissProgressDialog();

                        if (response.getStatus() == Constants.Status.SUCCESS) {
                            isRegisterSubscriptionDone = true;
                            //now query Google to update the product_name and the price
                            initBillingClient();
                        } else {
                            handleError(TAG + ".RegisterSubscriptionAfterPayment", response.getMessage());
                        }
                    }
                });
    }

    /**
     * Update subscription product name and price after it is registered successfully.
     * @param subscription user's subscription data
     */
    private void updateSubscription(final Subscription subscription) {
        subscriptionApi.updateSubscription(subscription)
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
                        handleError(TAG + ".UpdateSubscriptionAfterPayment", e.getMessage());
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
                                            IntentUtils.startHomeActivity(SubscriptionPlansActivity.this);
                                            finish();
                                        }
                                    }
                            );
                            AnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_SUBSCRIPTION_BACKEND_SUCCESS);
                        } else {
                            handleError(TAG + ".UpdateSubscriptionAfterPayment", response.getMessage());
                        }
                    }
                });
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
