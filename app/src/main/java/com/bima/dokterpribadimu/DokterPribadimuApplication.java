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
