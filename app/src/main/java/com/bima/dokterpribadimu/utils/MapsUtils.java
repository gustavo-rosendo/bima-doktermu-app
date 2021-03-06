package com.bima.dokterpribadimu.utils;

import android.content.Context;
import android.location.LocationManager;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apradanas.
 */
public class MapsUtils {

    public static List<LatLng> decodePolylines(String encoded) {

        List<LatLng> polylines = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            polylines.add(p);
        }

        return polylines;
    }

    public static boolean CheckGPSEnabled() {
        LocationManager locationManager = (LocationManager) DokterPribadimuApplication.getInstance()
                .getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
