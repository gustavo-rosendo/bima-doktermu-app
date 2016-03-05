package com.bima.dokterpribadimu.data.inappbilling;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.bima.dokterpribadimu.BuildConfig;
import com.bima.dokterpribadimu.utils.iabutil.IabBroadcastReceiver;
import com.bima.dokterpribadimu.utils.iabutil.IabBroadcastReceiver.IabBroadcastListener;
import com.bima.dokterpribadimu.utils.iabutil.IabHelper;
import com.bima.dokterpribadimu.utils.iabutil.IabHelper.OnIabPurchaseFinishedListener;
import com.bima.dokterpribadimu.utils.iabutil.IabResult;
import com.bima.dokterpribadimu.utils.iabutil.Inventory;
import com.bima.dokterpribadimu.utils.iabutil.Purchase;

/**
 * Created by apradanas on 3/2/16.
 */
public class BillingClient implements IabBroadcastListener {

    private static final String TAG = "BillingClient";
    private static final int RC_REQUEST = 10001;

    // SKU for our subscription
    static final String SKU_DOKTER_PRIBADIKU_MONTHLY = "dokterpribadimu_monthly_subscription_00";

    private Activity activity;

    private BillingInitializationListener billingInitializationListener;
    private QueryInventoryListener queryInventoryListener;
    private IabBroadcastReceiver broadcastReceiver;

    private IabHelper iabHelper;

    // Will the subscription auto-renew?
    private boolean autoRenewEnabled = false;

    // Tracks the currently owned subscription SKU, and the options in the Manage dialog
    private String currentSubscriptionSku = "";

    // Does the user have an active subscription to the Dokter PribadiKu plan?
    private static boolean isSubscribedToDokterPribadiKu = false;

    public void release() {
        if (broadcastReceiver != null && activity != null) {
            activity.unregisterReceiver(broadcastReceiver);
        }

        if (iabHelper != null) {
            iabHelper.dispose();
            iabHelper = null;
        }

        if (activity != null) {
            activity = null;
        }

        if (billingInitializationListener != null) {
            billingInitializationListener = null;
        }

        if (queryInventoryListener != null) {
            queryInventoryListener = null;
        }
    }

    public void init(Activity activity) {
        this.activity = activity;

        iabHelper = new IabHelper(activity, BuildConfig.BILL_KEY);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
            public void onIabSetupFinished(IabResult result) {
                if(!result.isSuccess()) {
                    Log.d(TAG, "Problem setting up In-App-Billing: " + result);
                    if (billingInitializationListener != null) {
                        billingInitializationListener.onSuccess();
                    }
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (iabHelper == null) {
                    if (billingInitializationListener != null) {
                        billingInitializationListener.onFailed();
                    }

                    return;
                }

                broadcastReceiver = new IabBroadcastReceiver(BillingClient.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                BillingClient.this.activity.registerReceiver(broadcastReceiver, broadcastFilter);

                Log.d(TAG, "Success! Finished setting up In-App-Billing: " + result);
                if (billingInitializationListener != null) {
                    billingInitializationListener.onSuccess();
                }
            }
        });
    }

    // Listener that's called when we finish querying the subscriptions we own
    IabHelper.QueryInventoryFinishedListener gotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (iabHelper == null) {
                if (queryInventoryListener != null) {
                    queryInventoryListener.onFailed();
                }

                return;
            }

            // Is it a failure?
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);

                if (queryInventoryListener != null) {
                    queryInventoryListener.onFailed();
                }

                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // First find out which subscription is auto renewing
            Purchase dokterPribadiKuMonthly = inventory.getPurchase(SKU_DOKTER_PRIBADIKU_MONTHLY);

            if (dokterPribadiKuMonthly != null && dokterPribadiKuMonthly.isAutoRenewing()) {
                currentSubscriptionSku = SKU_DOKTER_PRIBADIKU_MONTHLY;
                autoRenewEnabled = dokterPribadiKuMonthly.isAutoRenewing();
            } else {
                currentSubscriptionSku = "";
                autoRenewEnabled = false;
            }

            // The user is subscribed if the subscription exists, even if it is not auto renewing
            isSubscribedToDokterPribadiKu =
                    dokterPribadiKuMonthly != null
                            && verifyDeveloperPayload(dokterPribadiKuMonthly);
            Log.d(TAG, "User " + (isSubscribedToDokterPribadiKu ? "HAS" : "DOES NOT HAVE")
                    + " DokterPribadiKu subscription.");

            if (queryInventoryListener != null) {
                queryInventoryListener.onSuccess(isSubscribedToDokterPribadiKu);
            }
        }
    };


    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        queryInventoryAsync();
    }

    public void queryInventoryAsync() {
        iabHelper.queryInventoryAsync(gotInventoryListener);
    }

    public void launchSubscriptionPurchaseFlow(OnIabPurchaseFinishedListener listener) {
        iabHelper.launchSubscriptionPurchaseFlow(
                activity,
                SKU_DOKTER_PRIBADIKU_MONTHLY,
                RC_REQUEST,
                listener);
    }

    public void setBillingInitializationListener(BillingInitializationListener listener) {
        billingInitializationListener = listener;
    }

    public void setQueryInventoryListener(QueryInventoryListener listener) {
        queryInventoryListener = listener;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass on the activity result to the helper for handling
        return iabHelper.handleActivityResult(requestCode, resultCode, data);
    }

    public boolean isSubscribedToDokterPribadiKu() {
        return isSubscribedToDokterPribadiKu;
    }
}
