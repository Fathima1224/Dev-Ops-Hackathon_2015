/**
 * Copyright Trimble Inc., 2013 - 2014 All rights reserved.
 *
 *  Licensed Software Confidential and Proprietary Information of Trimble Inc., made available under Non-Disclosure Agreement OR License as applicable.
 *
 *  Product Name:
 * FSM Supervisor Application
 *
 *  Module Name:
 *  src/com/trimble/fsm/supervisor/GPSTracker.java
 *
 *  File name:
 *  GPSTracker.java
 *
 *  Author:
 *  Pavani Polakala
 *
 *  Created On:
 *  17/3/2014
 *
 *
 *  Abstract:
 *
 *
 *  Environment:
 *  Mobile Profile          :
 *  Mobile Configuration    :
 *
 *  Notes:
 *
 *
 *  Revision History:
 */

package com.trimble.fsm.fieldmaster.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;

import java.util.Calendar;

public class GPSTracker {

    public static final String LOCATION_AVAILABLE = "LOCATION_AVAILABLE";

    public static final String LOCATION_NOT_AVAILABLE = "LOCATION_NOT_AVAILABLE";

    public static final String LOCATION_SERVICE_NOT_ENABLED = "LOCATION_SERVICE_NOT_ENABLED";

    public static final String WAITING_FOR_LOCATION_RETRY = "WAITING_FOR_LOCATION_RETRY";

    /**
     * the location manager
     */
    private LocationManager locMgr = null;

    private static GPSTracker mLocationManager = null;
    public String jf;

    /**
     * latitude
     */
    private double dLat = 0.0;
    /**
     * longitude
     */
    private double dLon = 0.0;

    private Location mLocation = null;

    // private Location mFineLocation = null;
    // private Location mCoarseLocation = null;

    /**
     * @return the dLat
     */
    public double getLatitude() {
        return dLat;
    }

    /**
     * @param dLat the dLat to set
     */
    public void setdLat(double dLat) {
        this.dLat = dLat;
    }

    /**
     * @return the dLon
     */
    public double getLongitude() {
        return dLon;
    }

    /**
     * @param dLon the dLon to set
     */
    public void setdLon(double dLon) {
        this.dLon = dLon;
    }

    private GPSTracker(Context context) {
        // retrieve the location manager
        locMgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
    }

    public static synchronized GPSTracker getInstance() {

        if (mLocationManager == null) {
            Context context = ApplicationContextProvider.getContext();
            mLocationManager = new GPSTracker(context);


        }

        return mLocationManager;
    }

    public void startTracking() {
        try {
            Context context = ApplicationContextProvider.getContext();

            ComponentName comp = new ComponentName(context.getPackageName(), BackgroundLocationService.class.getName());
            ComponentName service = context.startService(new Intent().setComponent(comp));

            if (null == service) {
                // something really wrong here
                Log.e("GPSTracker", "Could not start service " + comp.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scheduleStopGPSTracking() {
        Context context = ApplicationContextProvider.getContext();

        //Create an offset from the current time in which the alarm will go off.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 120);

        //Create a new PendingIntent and add it to the AlarmManager
        Intent intent = new Intent(context, StopGPSReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                pendingIntent);

        //Toast.makeText(context, "Starting Alarm manager", Toast.LENGTH_SHORT ).show();
    }

    public void stopTracking() {
        try {
            Context context = ApplicationContextProvider.getContext();
            ComponentName comp = new ComponentName(context.getPackageName(), BackgroundLocationService.class.getName());
            context.stopService(new Intent().setComponent(comp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isGPSProviderEnabled() {
        return locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isGPSProviderAvailableOnDevice() {
        LocationProvider gpsProvider = locMgr
                .getProvider(LocationManager.GPS_PROVIDER);
        return (gpsProvider != null);
    }

    public String getLocationStatus() {
        boolean gpsProviderAvailableOnDevice = isGPSProviderAvailableOnDevice();
        if (gpsProviderAvailableOnDevice) {
            boolean gpsProviderEnabled = isGPSProviderEnabled();

            if (!gpsProviderEnabled) {
                return LOCATION_SERVICE_NOT_ENABLED;
            }
        } else {
            return LOCATION_SERVICE_NOT_ENABLED;
        }
        return LOCATION_AVAILABLE;

    }

}
