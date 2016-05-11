package com.bima.dokterpribadimu;

import android.app.Application;

import com.bima.dokterpribadimu.injection.DokterPribadimuComponent;
import com.crashlytics.android.Crashlytics;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

/**
 * Created by apradanas on 2/3/16.
 */
public class DokterPribadimuApplication extends Application {

    private static DokterPribadimuApplication sInstance;
    private DokterPribadimuComponent mComponent;

    /*
     * Google Analytics tracker
     */
    private Tracker mTracker;

    public static DokterPribadimuApplication getInstance() {
        return sInstance;
    }

    public static DokterPribadimuComponent getComponent() {
        return sInstance.mComponent;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        //Enable Advertising Features
        mTracker.enableAdvertisingIdCollection(true);
        return mTracker;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        //LeakCanary.install(this);

        sInstance = this;
        mComponent = DokterPribadimuComponent.Initializer.init(this);
    }
}
