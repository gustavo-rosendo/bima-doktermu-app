package com.bima.dokterpribadimu.view.activities;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.data.remote.api.DirectionsApi;
import com.bima.dokterpribadimu.model.Discount;
import com.bima.dokterpribadimu.model.directions.DirectionsResponse;
import com.bima.dokterpribadimu.model.directions.Leg;
import com.bima.dokterpribadimu.model.directions.Route;
import com.bima.dokterpribadimu.utils.MapsUtils;
import com.bima.dokterpribadimu.viewmodel.DiscountItemViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.PolylineOptions;

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

public class PartnersLandingActivity extends BaseActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private static final String TAG = PartnersLandingActivity.class.getSimpleName();

    private static final int RC_LOCATION_PERMISSION = 1;

    // Zoom level info: https://developers.google.com/maps/documentation/android-api/views#zoom
    private static final float DEFAULT_STREET_ZOOM_LEVEL = 15;

    private static final int CIRCLE_RADIUS_METER = 500;
    private static final int CIRCLE_STROKE_WIDTH = 2;

    private static final float DEFAULT_POLYLINE_WIDTH = 7;
    private static final String DIRECTIONS_PATTERN = "%s,%s";

    private static final int POPUP_BOTTOM_MARGIN = 400;

    @Inject
    PartnersApi partnersApi;

    @Inject
    DirectionsApi directionsApi;

    private ActivityPartnersLandingBinding binding;
    private DrawerFragment drawerFragment;

    private GoogleMap map;
    private Location location;
    private LocationTracker locationTracker;

    private boolean isLocationSet = false;

    private boolean isPolylineShowed = false;
    private List<Route> routes = new ArrayList<>();
    private Partner selectedPartner;

    private DiscountListViewModel discountListViewModel = new DiscountListViewModel();

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
        binding.setViewModel(discountListViewModel);

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
                    updateUserLocation(true);
                }
            }
        });

        binding.partnersFooterMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (map != null && location != null) {
                    // Move the camera to user's location
                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_STREET_ZOOM_LEVEL));
                }
            }
        });

        binding.partnersCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePolyLines();
            }
        });

        binding.partnersHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.partnersFooterLayout.getVisibility() == View.GONE) {
                    showDetailFooter();
                } else {
                    hideDetailFooter();
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

                            // reset selected
                            for (Category c : categories) {
                                c.setSelected(false);
                            }
                            category.setSelected(true);

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

    private void showDetailFooter() {
        if (hasDiscount()) {
            binding.partnersFooterDiscountLayout.setVisibility(View.VISIBLE);
        }
        binding.partnersFooterLayout.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams headerParams = (RelativeLayout.LayoutParams) binding.partnersHeaderLayout.getLayoutParams();
        headerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        headerParams.addRule(RelativeLayout.ABOVE, binding.partnersFooterLayout.getId());
        binding.partnersHeaderLayout.setLayoutParams(headerParams);
        binding.partnersHeaderLayout.setSelected(true);
        binding.partnersCarButton.setSelected(true);
        binding.partnersAddress.setText("Duration"); // TODO: change with category name
        binding.partnersMyLocationButton.setVisibility(View.GONE);
    }

    private void hideDetailFooter() {
        RelativeLayout.LayoutParams headerParams = (RelativeLayout.LayoutParams) binding.partnersHeaderLayout.getLayoutParams();
        headerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        headerParams.addRule(RelativeLayout.ABOVE, 0);
        binding.partnersHeaderLayout.setLayoutParams(headerParams);
        binding.partnersHeaderLayout.setSelected(false);
        binding.partnersCarButton.setSelected(false);
        if(selectedPartner != null) {
            binding.partnersAddress.setText(selectedPartner.getPartnerAddress());
        }
        binding.partnersMyLocationButton.setVisibility(View.VISIBLE);

        binding.partnersFooterLayout.setVisibility(View.GONE);
        binding.partnersFooterDiscountLayout.setVisibility(View.GONE);
    }

    private boolean hasDiscount() {
        if(selectedPartner != null) {
            return selectedPartner.getDiscount().size() > 0;
        }
        return false;
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

                            if(partnerResponse.getData() == null) {
                                Toast.makeText(
                                        DokterPribadimuApplication.getInstance().getApplicationContext(),
                                        DokterPribadimuApplication.getInstance().getResources().getString(R.string.partners_none_found),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                            else {
                                partners.addAll(partnerResponse.getData().getPartner());
                            }

                            updateUserLocation(true);
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
                        updateUserLocation(true);
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

    private void updateUserLocation(boolean moveToUserLocation) {
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

                map.addCircle(new CircleOptions()
                        .center(userLatLng)
                        .radius(CIRCLE_RADIUS_METER)
                        .strokeWidth(CIRCLE_STROKE_WIDTH)
                        .strokeColor(ContextCompat.getColor(this, R.color.bima_blue))
                        .fillColor(ContextCompat.getColor(this, R.color.bima_blue_alpha)));

                if (moveToUserLocation) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_STREET_ZOOM_LEVEL));
                }
            }

            // Add partners if there are any, independently of the user's location
            if (partners.size() > 0) {
                addPartnersMarker();
            }
        }
    }

    private void updatePolyLines() {
        if(!isPolylineShowed) {
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
                isPolylineShowed = true;
            }
        }
    }

    private void addPartnersMarker() {
        for (Partner partner : partners) {
            try {
                Double latitude = Double.parseDouble(partner.getPartnerLat());
                Double longitude = Double.parseDouble(partner.getPartnerLong());
                LatLng userLatLng = new LatLng(latitude, longitude);
                map.addMarker(
                        new MarkerOptions()
                                .position(userLatLng)
                                .title(partner.getPartnerName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
                );
            } catch (NumberFormatException e) {
                Log.e(TAG, "Could not add marker for Partner '" + partner.getPartnerName()
                        + "' - error message: "
                        + e.getMessage());
                e.printStackTrace();
            }
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                String title = marker.getTitle();
                LatLng markerLatLng = marker.getPosition();
                if (!title.equalsIgnoreCase(getString(R.string.your_location))) {
                    for (Partner partner : partners) {
                        Double latitude = Double.parseDouble(partner.getPartnerLat());
                        Double longitude = Double.parseDouble(partner.getPartnerLong());
                        LatLng userLatLng = new LatLng(latitude, longitude);
                        if (userLatLng.latitude == markerLatLng.latitude
                                && userLatLng.longitude == markerLatLng.longitude) {
                            selectedPartner = partner;

                            // call API to get the directions to the selected partner
                            setupDirectionRequest(partner);

                            // set the details of the selected partner
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

                            binding.partnersMyLocationButton.setVisibility(View.GONE);
                            binding.partnersMenuButton.setVisibility(View.GONE);
                            binding.partnersFooterRelativeLayout.setVisibility(View.VISIBLE);
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        map.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener(){
            @Override
            public void onInfoWindowClose(Marker marker) {
                binding.partnersFooterRelativeLayout.setVisibility(View.GONE);
                binding.partnersMyLocationButton.setVisibility(View.VISIBLE);
                binding.partnersMenuButton.setVisibility(View.VISIBLE);

                if (map != null) {
                    isPolylineShowed = false;
                    updateUserLocation(false);
                }
            }
        });
    }

    private void setupDirectionRequest(Partner partner) {
        if(location != null) {
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
                                PartnersLandingActivity.this,
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
                    }
                });
    }

    public static class DiscountListViewModel {
        public final ObservableList<DiscountItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.discount_item_viewmodel, R.layout.item_discount);
    }

}
