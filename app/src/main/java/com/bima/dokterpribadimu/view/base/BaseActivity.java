package com.bima.dokterpribadimu.view.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by apradanas on 2/3/16.
 */
public class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/
}
