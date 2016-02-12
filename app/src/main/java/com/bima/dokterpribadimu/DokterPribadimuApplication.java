package com.bima.dokterpribadimu;

import android.app.Application;

import com.bima.dokterpribadimu.injection.DokterPribadimuComponent;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());

        sInstance = this;
        mComponent = DokterPribadimuComponent.Initializer.init(this);

        //initFont();
    }

    private void initFont() {
        //Initialize Calligraphy library
        //Set default font into OpenSans-Regular
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
