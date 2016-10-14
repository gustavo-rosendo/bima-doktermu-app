package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.bima.dokterpribadimu.BuildConfig;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.data.remote.api.CustomerApi;
import com.bima.dokterpribadimu.databinding.ActivityHomeBinding;
import com.bima.dokterpribadimu.messaging.FirebaseInstanceIDService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;
import com.bima.dokterpribadimu.view.fragments.HomeFragment;
import com.bima.dokterpribadimu.BuildConfig;
import com.bima.dokterpribadimu.messaging.FirebaseInstanceIDService;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.model.BaseResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding binding;
    private DrawerFragment drawerFragment;

    @Inject
    CustomerApi customerApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        initViews();
        setupDrawerFragment();
        setupHomeFragment();
        runFirebaseTokenCheck();
    }

    private void initViews() {
        binding.toolbarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_MENU_DRAWER);
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void setupDrawerFragment() {
        drawerFragment = DrawerFragment.newInstance(Constants.DRAWER_TYPE_HOME);
        drawerFragment.setOnDrawerItemPressedListener(new DrawerFragment.OnDrawerItemPressed() {
            @Override
            public void onDrawerItemPressed(int selectedDrawerType) {
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_drawer, drawerFragment)
                .commit();
    }

    private void setupHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_container, HomeFragment.newInstance())
                .commit();
    }

    @Override
    protected void onDestroy() {
        drawerFragment.setOnDrawerItemPressedListener(null);

        super.onDestroy();
    }

    /**
     * Check to see if we have ever received a firebase token if not get it and save it.
     */
    private void runFirebaseTokenCheck() {

        String firebaseToken = "";
        String storedFirebaseToken = "";
        Boolean firebaseTokenSaved = false;
        String accessToken = "";

        //Get the token assigned by the firebase service
        firebaseToken = FirebaseInstanceIDService.getToken();

        //Get the token which we stored in the storage utils
        storedFirebaseToken = StorageUtils.getString(HomeActivity.this,
                Constants.KEY_FIREBASE_ACCESS_TOKEN,
                "");

        //If we are in debug mode always save the token to the database. This is just incase we are switching backend databases during development
        if(!BuildConfig.DEBUG) {
            firebaseTokenSaved = StorageUtils.getBoolean(HomeActivity.this,
                    Constants.KEY_FIREBASE_ACCESS_TOKEN_SAVED,
                    false);
        }

        //If the firebase tokens are different then we need to save the latest to the database
        if(!firebaseToken.equals(storedFirebaseToken) || !firebaseTokenSaved)
        {
            UserProfile user_profile;

            user_profile = UserProfileUtils.getUserProfile(HomeActivity.this);
            if(user_profile != null) {

                accessToken = UserProfileUtils.getUserProfile(HomeActivity.this).getAccessToken();

                if (!accessToken.isEmpty()) {

                    saveFirebaseToken(firebaseToken, accessToken);

                    StorageUtils.putString(HomeActivity.this,
                            Constants.KEY_FIREBASE_ACCESS_TOKEN,
                            firebaseToken);

                    StorageUtils.putBoolean(HomeActivity.this,
                            Constants.KEY_FIREBASE_ACCESS_TOKEN_SAVED,
                            true);

                }
            }
        }

    }

    /**
     * Do save firebase token.
     * @param firebaseToken google firebase messaging client access token
     * @param accessToken user's access token
     */
    private void saveFirebaseToken(final String firebaseToken, final String accessToken) {
        customerApi.changeFirebaseToken(firebaseToken, accessToken)
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
                        handleError("HomeActivity", e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse response) {

                        if (response.getStatus() == Constants.Status.SUCCESS) {
                            StorageUtils.putBoolean(HomeActivity.this,
                                    Constants.KEY_FIREBASE_ACCESS_TOKEN_SAVED,
                                    true);

                        } else {
                            StorageUtils.putBoolean(HomeActivity.this,
                                    Constants.KEY_FIREBASE_ACCESS_TOKEN_SAVED,
                                    false);
                            handleError("HomeActivity", response.getMessage());
                        }
                    }
                });
    }
}
