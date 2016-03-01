package com.bima.dokterpribadimu;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.bima.dokterpribadimu.injection.DokterPribadimuComponent;

import com.bima.dokterpribadimu.util.IabHelper;
import com.bima.dokterpribadimu.util.IabResult;
import com.bima.dokterpribadimu.util.Inventory;
import com.bima.dokterpribadimu.util.Purchase;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by apradanas on 2/3/16.
 */
public class DokterPribadimuApplication extends Application {

    private static DokterPribadimuApplication sInstance;
    private DokterPribadimuComponent mComponent;

    public static DokterPribadimuApplication getInstance() {
        return sInstance;
    }

    public static DokterPribadimuComponent getComponent() {
        return sInstance.mComponent;
    }

    public static final String TAG = "DokterPribadiMu";

    IabHelper mHelper;

    // SKU for our subscription
    static final String SKU_DOKTER_PRIBADIKU_MONTHLY = "ds1_bimamobile_monthly_subscription_00";

    // Will the subscription auto-renew?
    boolean mAutoRenewEnabled = false;

    // Tracks the currently owned subscription SKU, and the options in the Manage dialog
    String mCurrentSubscriptionSku = "";

    // Does the user have an active subscription to the Dokter PribadiKu plan?
    boolean mSubscribedToDokterPribadiKu = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        sInstance = this;
        mComponent = DokterPribadimuComponent.Initializer.init(this);

        //TODO : do NOT hardcode the public key! Enhance the security computing it in a different manner
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj1oiLMNCvso09PcCv4RxGmtKOfYZLR4NrwQ/1FcuRX4vkfYvu9MEZA5eqUpS0NtTnHQGq0YfsAYeZ560bLweADhJUbo+nIIChOLPnnuWYE+HKbKz82jQN+ZCaN06FgSSAtmDz1JUTMVeZasn0LQ3XXa65WF7CozFB1VasyxumYmf3cOtVi/9z+tSxqS3TIxgPn6AMQvcupZ4v5g84vnW2mNdh5Jo55mfhhFko//+JbYIV5BZjyKuHTpPKUxyqPLCToy9D5PyDrK1sNRRQtUu5ihiDWVFbz8ddJCG46o6w9xIebmJQO+/QzkBtjwd/babg83+AHam0OsqfPRaGcAfIwIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
            public void onIabSetupFinished(IabResult result) {
                if(!result.isSuccess()) {
                    Log.d(TAG, "Problem setting up In-App-Billing: " + result);
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                Log.d(TAG, "Success! Finished setting up In-App-Billing: " + result);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    // Listener that's called when we finish querying the subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
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
                mCurrentSubscriptionSku = SKU_DOKTER_PRIBADIKU_MONTHLY;
                mAutoRenewEnabled = true;
            } else {
                mCurrentSubscriptionSku = "";
                mAutoRenewEnabled = false;
            }

            // The user is subscribed if the subscription exists, even if it is not auto renewing
            mSubscribedToDokterPribadiKu = (dokterPribadiKuMonthly != null && verifyDeveloperPayload(dokterPribadiKuMonthly));
            Log.d(TAG, "User " + (mSubscribedToDokterPribadiKu ? "HAS" : "DOES NOT HAVE")
                    + " DokterPribadiKu subscription.");

            if (mSubscribedToDokterPribadiKu) {
                //TODO : Skip Subscription flow and directly enable Book a Call
            }
            else {
                //TODO : Subscription flow (new or renew) should be enabled in this case
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
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
    public void onTerminate() {
        super.onTerminate();

        // very important to dispose the helper!
        Log.d(TAG, "Destroying helper.");
        if(mHelper != null) mHelper.dispose();
        mHelper = null;
    }
}
