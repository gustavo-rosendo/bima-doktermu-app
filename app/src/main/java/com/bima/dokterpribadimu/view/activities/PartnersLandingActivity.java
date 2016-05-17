package com.bima.dokterpribadimu.view.activities;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.PartnersApi;
import com.bima.dokterpribadimu.databinding.ActivityPartnersLandingBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CategoriesResponse;
import com.bima.dokterpribadimu.model.Category;
import com.bima.dokterpribadimu.model.Partner;
import com.bima.dokterpribadimu.model.PartnerResponse;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.CategoriesPopupWindow;
import com.bima.dokterpribadimu.view.components.CategoriesPopupWindow.CategoryClickListener;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PartnersLandingActivity extends BaseActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private static final String TAG = PartnersLandingActivity.class.getSimpleName();

    private static final int RC_LOCATION_PERMISSION = 1;

    // Zoom level info: https://developers.google.com/maps/documentation/android-api/views#zoom
    private static final float DEFAULT_STREET_ZOOM_LEVEL = 15;

    private static final int CIRCLE_RADIUS_METER = 500;
    private static final int CIRCLE_STROKE_WIDTH = 2;

    private static final int POPUP_BOTTOM_MARGIN = 400;

    @Inject
    PartnersApi partnersApi;

    private ActivityPartnersLandingBinding binding;
    private DrawerFragment drawerFragment;

    private GoogleMap map;
    private Location location;
    private LocationTracker locationTracker;

    private boolean isLocationSet = false;

    private CategoriesPopupWindow categoriesPopupWindow;

    private List<Category> categories = new ArrayList<>();
    private List<Partner> partners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_partners_landing);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        initViews();
        setupDrawerFragment();
        setupMapFragment();

        getCategories(UserProfileUtils.getUserProfile(this).getAccessToken());
    }

    private void initViews() {
        binding.toolbarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        binding.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startPartnersSearchActivity(PartnersLandingActivity.this);
            }
        });

        binding.partnersMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (map != null && location != null) {
                    moveToUserLocation();
                }
            }
        });

        categoriesPopupWindow = new CategoriesPopupWindow(PartnersLandingActivity.this);

        binding.partnersMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categories.size() > 0) {
                    binding.appbar.setVisibility(View.INVISIBLE);
                    binding.overlayView.setVisibility(View.VISIBLE);
                    binding.partnersMenuButton.setSelected(true);

                    categoriesPopupWindow.setCategories(categories);
                    categoriesPopupWindow.setClickListener(new CategoryClickListener() {
                        @Override
                        public void onClick(Category category) {
                            categoriesPopupWindow.dismiss();

                            getPartners(
                                    category.getCategoryName(),
                                    UserProfileUtils.getUserProfile(PartnersLandingActivity.this).getAccessToken());
                        }
                    });
                    categoriesPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            binding.appbar.setVisibility(View.VISIBLE);
                            binding.overlayView.setVisibility(View.GONE);
                            binding.partnersMenuButton.setSelected(false);
                        }
                    });
                    categoriesPopupWindow.showAtLocation(
                            binding.partnersMenuButton,
                            Gravity.BOTTOM|Gravity.CENTER,
                            0,
                            POPUP_BOTTOM_MARGIN
                    );
                }
            }
        });
    }

    private void setupDrawerFragment() {
        drawerFragment = DrawerFragment.newInstance(Constants.DRAWER_TYPE_PARTNERS);
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

    private void setupMapFragment() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_container, mapFragment)
                .commit();
    }


    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be
     * used. This is where we can add markers or lines, add listeners or move the camera. In this
     * case, we just add a marker near Sydney, Australia. If Google Play services is not installed
     * on the device, the user will be prompted to install it inside the SupportMapFragment. This
     * method will only be triggered once the user has installed Google Play services and returned
     * to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // disable map toolbar and zoom buttons
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);

        binding.partnersMyLocationButton.setVisibility(View.VISIBLE);
        binding.partnersMenuButton.setVisibility(View.VISIBLE);

        initLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (locationTracker != null) {
            locationTracker.startListening();
        }
    }

    @Override
    protected void onPause() {
        if (locationTracker != null) {
            locationTracker.stopListening();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (locationTracker != null) {
            locationTracker.stopListening();
        }

        if (categoriesPopupWindow != null) {
            categoriesPopupWindow.release();
        }

        drawerFragment.setOnDrawerItemPressedListener(null);

        super.onDestroy();
    }

    /**
     * Get partners categories
     * @param accessToken user's access token
     */
    private void getCategories(final String accessToken) {
        partnersApi.getCategories(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<CategoriesResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<CategoriesResponse>>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<CategoriesResponse> categoriesResponse) {
                        if (categoriesResponse.getStatus() == Constants.Status.SUCCESS) {
                            categories = categoriesResponse.getData().getCategories();
                        } else {
                            handleError(TAG, categoriesResponse.getMessage());
                        }
                    }
                });
    }

    /**
     * Get partners categories
     * @param accessToken user's access token
     */
    private void getPartners(final String partnerCategory, final String accessToken) {
        partnersApi.getPartners(partnerCategory, "", accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<PartnerResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<PartnerResponse>>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<PartnerResponse> partnerResponse) {
                        dismissProgressDialog();

                        if (partnerResponse.getStatus() == Constants.Status.SUCCESS) {
                            partners.clear();
                            partners.addAll(partnerResponse.getData().getPartner());

                            moveToUserLocation();
                        } else {
                            handleError(TAG, partnerResponse.getMessage());
                        }
                    }
                });
    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    public void initLocation() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationTracker();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RC_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationTracker() {
        TrackerSettings settings =
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true);

        try {
            locationTracker = new LocationTracker(this, settings) {

                @Override
                public void onLocationFound(@NonNull Location location) {
                    PartnersLandingActivity.this.location = location;

                    if (!isLocationSet) {
                        isLocationSet = true;
                        moveToUserLocation();
                    }
                }

                @Override
                public void onTimeout() {
                    startLocationTracker();
                }
            };
            locationTracker.startListening();
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private void moveToUserLocation() {
        if (map != null) {
            map.clear();
            // Add a marker in user's location and move the camera
            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(
                    new MarkerOptions()
                            .position(userLatLng)
                            .title(getString(R.string.your_location))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_pin))
            );
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_STREET_ZOOM_LEVEL));

            map.addCircle(new CircleOptions()
                    .center(userLatLng)
                    .radius(CIRCLE_RADIUS_METER)
                    .strokeWidth(CIRCLE_STROKE_WIDTH)
                    .strokeColor(ContextCompat.getColor(this, R.color.bima_blue))
                    .fillColor(ContextCompat.getColor(this, R.color.bima_blue_alpha)));

            if (partners.size() > 0) {
                addPartnersMarker();
            }
        }
    }

    private void addPartnersMarker() {
        for (Partner partner : partners) {
            Double latitude = Double.parseDouble(partner.getPartnerLat());
            Double longitude = Double.parseDouble(partner.getPartnerLong());
            LatLng userLatLng = new LatLng(latitude, longitude);
            map.addMarker(
                new MarkerOptions()
                        .position(userLatLng)
                        .title(partner.getPartnerName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
            );
        }
    }
}
