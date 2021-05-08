package com.example.weather.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import com.example.weather.view.MainActivity;

import androidx.core.content.ContextCompat;

public class UIHelper {

    /**
     * check location run time permission is given
     * @param context
     * @return
     */
    public static boolean isLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check GPS is enabled
     * @param context
     * @return
     */
    public static boolean isLocationEnabled(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Check location run time permission and GPS is enabled
     * @param context
     * @return
     */
    public static boolean isEnabled(Context context) {
        return !isLocationPermission(context) && isLocationEnabled(context);
    }
}
