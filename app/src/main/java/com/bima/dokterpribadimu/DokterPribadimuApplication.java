package com.bima.dokterpribadimu;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bima.dokterpribadimu.analytics.TagManagerHelper;
import com.bima.dokterpribadimu.injection.DokterPribadimuComponent;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by apradanas on 2/3/16.
 */
public class DokterPribadimuApplication extends Application {

    private static DokterPribadimuApplication sInstance;
    private DokterPribadimuComponent mComponent;

    // Google Analytics tracker
    private Tracker mGoogleAnalytics;

    // FirebaseAnalytics instance
    private FirebaseAnalytics mFirebaseAnalytics;

    public static DokterPribadimuApplication getInstance() {
        return sInstance;
    }

    public static DokterPribadimuComponent getComponent() {
        return sInstance.mComponent;
    }

    /**
     * Gets the default {@link FirebaseAnalytics} for this {@link Application}.
     * @return mFirebaseAnalytics
     */
    public FirebaseAnalytics getDefaultFirebaseAnalytics() {
        if (mFirebaseAnalytics == null) {
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }
        return mFirebaseAnalytics;
    }

    /**
     * Gets the default GoogleAnalytics instance for this {@link Application}.
     * @return mTracker
     */
    synchronized public Tracker getDefaultGATracker() {
        if (mGoogleAnalytics == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mGoogleAnalytics = analytics.newTracker(R.xml.global_tracker);
        }
        //Enable Advertising Features
        mGoogleAnalytics.enableAdvertisingIdCollection(true);
        return mGoogleAnalytics;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        //LeakCanary.install(this);

        sInstance = this;
        mComponent = DokterPribadimuComponent.Initializer.init(this);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = getDefaultFirebaseAnalytics();

        // Obtain the GoogleAnalytics instance.
        mGoogleAnalytics = getDefaultGATracker();

        // Initialize TagManager and load the container from the web
        // (if loading from the web fails, load the default container saved in res/raw)
        //TagManagerHelper.initializeTagManager(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        if(BuildConfig.DEBUG) {
            MultiDex.install(this);
        }
    }
}
