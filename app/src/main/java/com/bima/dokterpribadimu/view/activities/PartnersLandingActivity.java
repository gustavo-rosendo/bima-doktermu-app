package com.bima.dokterpribadimu.view.activities;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityPartnersLandingBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;

import java.util.List;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PartnersLandingActivity extends BaseActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private static final int RC_LOCATION_PERMISSION = 1;

    // Zoom level info: https://developers.google.com/maps/documentation/android-api/views#zoom
    private static final float DEFAULT_STREET_ZOOM_LEVEL = 15;

    private static final int CIRCLE_RADIUS_METER = 500;
    private static final int CIRCLE_STROKE_WIDTH = 2;

    private ActivityPartnersLandingBinding binding;
    private DrawerFragment drawerFragment;

    private GoogleMap map;
    private Location location;
    private LocationTracker locationTracker;

    private boolean isLocationSet = false;

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
    }

    private void initViews() {
        binding.toolbarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
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

        binding.partnersMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: show partners category menu
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
    protected void onDestroy() {
        if (locationTracker != null) {
            locationTracker.stopListening();
        }

        super.onDestroy();
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
        }
    }
}
