package com.trimble.fsm.fieldmaster.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BLakshm on 18-07-2014.
 */
public class StopGPSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, DateFormat.getDateTimeInstance().format(new Date()) + "Stopped by Alarm Manager", Toast.LENGTH_SHORT).show();
        GPSTracker.getInstance().stopTracking();
    }
}
