package com.bima.dokterpribadimu;

import android.app.Application;
import android.util.Log;

import com.bima.dokterpribadimu.injection.DokterPribadimuComponent;

import com.bima.dokterpribadimu.util.IabHelper;
import com.bima.dokterpribadimu.util.IabResult;
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

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        sInstance = this;
        mComponent = DokterPribadimuComponent.Initializer.init(this);

        //TODO : do NOT hardcode the public key! Enhance the security computing it in a different manner
        String base64EncodedPublicKey = "";

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
            public void onIabSetupFinished(IabResult result) {
                if(!result.isSuccess()) {
                    Log.d(TAG, "Problem setting up In-App-Billing: " + result);
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                Log.d(TAG, "Success! Finished setting up In-App-Billing: " + result);

                //TODO : Query purchased items
            }
        });
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
