package com.bima.dokterpribadimu.view.activities;


import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.analytics.FirebaseAnalyticsHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityLoadingBinding;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class LoadingActivity extends BaseActivity {

    private static final String TAG = LoadingActivity.class.getSimpleName();

    private static final int SLEEP_DURATION = 4;
    private static final int ANIMATION_DURATION = 1000;
    private static final float ANIMATE_FROM = 0.9f;
    private static final float ANIMATE_TO = 1.2f;
    private static final String SCALE_X_ANIMATION = "scaleX";
    private static final String SCALE_Y_ANIMATION = "scaleY";

    private ActivityLoadingBinding binding;

    private AnimatorSet scaleAnim;

//    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loading);

        // Obtain the shared Tracker instance.
//        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();

        initAnim();
        runSplash();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Log.d(TAG, "Setting screen name: " + TAG);
//        mTracker.setScreenName("Image~" + TAG);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        FirebaseAnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_LOADING);
    }

    @Override
    protected void onDestroy() {
        scaleAnim.end();
        scaleAnim = null;

        super.onDestroy();
    }

    private void initAnim() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
                                    binding.loadingBimaIcon,
                                    SCALE_X_ANIMATION,
                                    ANIMATE_FROM,
                                    ANIMATE_TO
                                );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
                                    binding.loadingBimaIcon,
                                    SCALE_Y_ANIMATION,
                                    ANIMATE_FROM,
                                    ANIMATE_TO
                                );

        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        scaleAnim = new AnimatorSet();
        scaleAnim.setDuration(ANIMATION_DURATION);
        scaleAnim.play(scaleX).with(scaleY);

        scaleAnim.start();
    }

    private void runSplash() {
        getSplashObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindToLifecycle())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        UserProfile userProfile = GsonUtils.fromJson(
                                StorageUtils.getString(LoadingActivity.this, Constants.KEY_USER_PROFILE, ""),
                                UserProfile.class
                        );

                        if (userProfile == null) {
                            IntentUtils.startLandingActivity(LoadingActivity.this);
                        } else {
                            IntentUtils.startHomeActivityOnTop(LoadingActivity.this);
                        }

                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String string) {
                    }
                });
    }

    /**
     * Just sleep SLEEP_DURATION seconds
     * @return String observable
     */
    private Observable<String> getSplashObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override public Observable<String> call() {
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(SLEEP_DURATION));
                } catch (InterruptedException e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just("");
            }
        });
    }
}
