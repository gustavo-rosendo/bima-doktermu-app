package com.bima.dokterpribadimu.view.activities;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.DirectionsApi;
import com.bima.dokterpribadimu.databinding.ActivityPartnersDetailBinding;
import com.bima.dokterpribadimu.model.Discount;
import com.bima.dokterpribadimu.model.Partner;
import com.bima.dokterpribadimu.model.directions.DirectionsResponse;
import com.bima.dokterpribadimu.model.directions.Leg;
import com.bima.dokterpribadimu.model.directions.Route;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.MapsUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.viewmodel.DiscountItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import me.tatarka.bindingcollectionadapter.ItemView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PartnersDetailActivity extends BaseActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private static final String TAG = PartnersDetailActivity.class.getSimpleName();

    private static final String PARTNER = "partner";

    private static final int RC_LOCATION_PERMISSION = 1;

    // Zoom level info: https://developers.google.com/maps/documentation/android-api/views#zoom
    private static final float DEFAULT_STREET_ZOOM_LEVEL = 15;

    private static final float DEFAULT_POLYLINE_WIDTH = 7;

    private static final String DIRECTIONS_PATTERN = "%s,%s";

    @Inject
    DirectionsApi directionsApi;

    private ActivityPartnersDetailBinding binding;

    private GoogleMap map;
    private Location location;
    private LocationTracker locationTracker;

    private Partner partner;
    private DiscountListViewModel discountListViewModel = new DiscountListViewModel();

    private boolean isPolylineShowed = false;
    private List<Route> routes = new ArrayList<>();

    public static Intent create(Context context, Partner partner) {
        Intent intent = new Intent(context, PartnersDetailActivity.class);
        intent.putExtra(PARTNER, GsonUtils.toJson(partner));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_partners_detail);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        partner = GsonUtils.fromJson(getIntent().getExtras().getString(PARTNER), Partner.class);

        initViews();
        setupMapFragment();
    }

    private void initViews() {
        binding.setViewModel(discountListViewModel);

        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.partnersMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserLocation(true, isPolylineShowed);
            }
        });

        binding.partnersCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPolylineShowed = !isPolylineShowed;
                updateUserLocation(false, isPolylineShowed);
            }
        });

        binding.partnersDetailHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.partnersDetailFooterLayout.getVisibility() == View.GONE) {
                    showDetailFooter();
                } else {
                    hideDetailFooter();
                }
            }
        });

        binding.toolbarTitle.setText(partner.getPartnerName());

        binding.partnersName.setText(partner.getPartnerName());
        binding.partnersAddress.setText(partner.getPartnerAddress());

        binding.partnersFooterAddress.setText(partner.getPartnerAddress());

        String discountAmount = "-";
        if (hasDiscount()) {
            float lastDiscountValue = 0;
            // Clear list before adding to avoid double items
            discountListViewModel.items.clear();
            for (Discount discount : partner.getDiscount()) {
                discountListViewModel.items.add(
                        new DiscountItemViewModel(discount)
                );

                //Search for the biggest discount value
                String aux = discount.getDiscount().replace(" ", "");
                aux = aux.replace("%", "");
                try {
                    float discountValue = Float.parseFloat(aux);
                    if(discountValue > lastDiscountValue) {
                        discountAmount = String.valueOf(discountValue);
                        discountAmount.replace(".0", ""); // erase the decimal if the number is rounded. Ex.: 10.0
                        discountAmount += "%";
                        lastDiscountValue = discountValue;
                    }
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Error when trying to parse discount value. Message: " + e.getMessage());
                    e.printStackTrace();
                    discountAmount = aux.equalsIgnoreCase("-") ? aux : (aux + "%");
                }
            }
        }
        binding.partnersDiscount.setText(discountAmount);
    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_container, mapFragment)
                .commit();
    }

    private void showDetailFooter() {
        if (hasDiscount()) {
            binding.partnersFooterDiscountLayout.setVisibility(View.VISIBLE);
        }
        binding.partnersDetailFooterLayout.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams headerParams = (RelativeLayout.LayoutParams) binding.partnersDetailHeaderLayout.getLayoutParams();
        headerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        headerParams.addRule(RelativeLayout.ABOVE, binding.partnersDetailFooterLayout.getId());
        binding.partnersDetailHeaderLayout.setLayoutParams(headerParams);
        binding.partnersDetailHeaderLayout.setSelected(true);
        binding.partnersCarButton.setSelected(true);
        binding.partnersAddress.setText("Duration"); // TODO: change with category name
        binding.partnersMyLocationButton.setVisibility(View.GONE);
    }

    private void hideDetailFooter() {
        RelativeLayout.LayoutParams headerParams = (RelativeLayout.LayoutParams) binding.partnersDetailHeaderLayout.getLayoutParams();
        headerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        headerParams.addRule(RelativeLayout.ABOVE, 0);
        binding.partnersDetailHeaderLayout.setLayoutParams(headerParams);
        binding.partnersDetailHeaderLayout.setSelected(false);
        binding.partnersCarButton.setSelected(false);
        binding.partnersAddress.setText(partner.getPartnerAddress());
        binding.partnersMyLocationButton.setVisibility(View.VISIBLE);

        binding.partnersDetailFooterLayout.setVisibility(View.GONE);
        binding.partnersFooterDiscountLayout.setVisibility(View.GONE);
    }

    private boolean hasDiscount() {
        return partner.getDiscount().size() > 0;
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

        super.onDestroy();
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

        initLocation();
        addPartnersMarker(true);
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
                    PartnersDetailActivity.this.location = location;

                    setupDirectionRequest();
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

    private void updateUserLocation(boolean moveToUserLocation, boolean updatePolylines) {
        if (map != null) {
            map.clear();
            if(location != null) {
                // Add a marker in user's location and move the camera
                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(
                        new MarkerOptions()
                                .position(userLatLng)
                                .title(getString(R.string.your_location))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_pin))
                );

                if (moveToUserLocation) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_STREET_ZOOM_LEVEL));
                }

                if (updatePolylines) {
                    updatePolyLines();
                }
            }

            // Add partners if there are any, independently of the user's location
            addPartnersMarker(false);
        }
    }

    private void addPartnersMarker(boolean animateCamera) {
        Double latitude = Double.parseDouble(partner.getPartnerLat());
        Double longitude = Double.parseDouble(partner.getPartnerLong());
        LatLng partnerLatLng = new LatLng(latitude, longitude);
        map.addMarker(
                new MarkerOptions()
                        .position(partnerLatLng)
                        .title(partner.getPartnerName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
        );

        if (animateCamera) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(partnerLatLng, DEFAULT_STREET_ZOOM_LEVEL));
        }
    }

    private void updatePolyLines() {
        if (routes.size() > 0) {
            Route route = routes.get(0);
            List<LatLng> polylines = MapsUtils.decodePolylines(route.getOverviewPolyline().getPoints());
            map.addPolyline(
                    new PolylineOptions()
                            .width(DEFAULT_POLYLINE_WIDTH)
                            .color(ContextCompat.getColor(this, R.color.bima_blue))
                            .geodesic(true)
                            .addAll(polylines)
            );
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private void setupDirectionRequest() {
        String origin = String.format(
                            Locale.US,
                            DIRECTIONS_PATTERN,
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()));
        String destination = String.format(
                                Locale.US,
                                DIRECTIONS_PATTERN,
                                partner.getPartnerLat(),
                                partner.getPartnerLong());
        getDirections(false, origin, destination);
    }

    private void getDirections(final boolean sensor, final String origin, final String destination) {
        directionsApi.getDirections(sensor, origin, destination)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<DirectionsResponse>bindToLifecycle())
                .subscribe(new Subscriber<DirectionsResponse>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                PartnersDetailActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onNext(DirectionsResponse partnerResponse) {
                        String duration = "";
                        routes = partnerResponse.getRoutes();
                        if (routes.size() > 0) {
                            List<Leg> legs = routes.get(0).getLegs();
                            if (legs.size() > 0) {
                                duration = legs.get(0).getDuration().getText();
                            }
                        }
                        binding.partnersDuration.setText(duration);

                        updateUserLocation(false, isPolylineShowed);
                    }
                });
    }

    public static class DiscountListViewModel {
        public final ObservableList<DiscountItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.discount_item_viewmodel, R.layout.item_discount);
    }
}
