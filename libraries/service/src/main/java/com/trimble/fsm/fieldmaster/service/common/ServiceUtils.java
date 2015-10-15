/**
 * Copyright Trimble Inc., 2011 - 2012 All rights reserved.
 * 
 * Licensed Software Confidential and Proprietary Information of Spime Inc.,
 * made available under Non-Disclosure Agreement OR License as applicable.
 * 
 * Product Name:
 * 
 * 
 * Module Name: com.trimble.JSONRPCService.common
 * 
 * File name: Utils.java
 * 
 * Author: Pavani Polakala
 * 
 * Created On: 25/04/2014
 * 
 * Abstract: Utils class contains common utility functions that are used in the
 * project.
 * 
 * Environment: Mobile Profile : Android
 * 
 * Notes:
 * 
 * Revision History:
 * 
 * 
 */
package com.trimble.fsm.fieldmaster.service.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.AndroidRuntimeException;

import com.trimble.fsm.fieldmaster.service.ApplicationContextProvider;
import com.trimble.fsm.fieldmaster.service.ServiceHelper;

public class ServiceUtils {

   public static final String AUTH_COOKIES_PARAM       = "auth_cookies_param";
   public static final String REST_COOKIES_PARAM       = "rest_cookies_param";
   public static final String REST_COOKIES_EXPIRY_TIME = "rest_cookies_expiry_time";
   public static final String SERVICE_ACTION_RESULT    = "service_action_result";
   public static final int    RPC_REAUTHENTICATE       = 0;
   public static final int    REST_REAUTHENTICATE      = 1;
   public static final int    USER_LOGOUT              = 2;

   /**
    * To convert the InputStream to String we use the BufferedReader.readLine()
    * method. We iterate until the BufferedReader return null which means
    * there's no more data to read. Each line will appended to a StringBuilder
    * and returned as String.
    */
   public static String convertStreamToString(InputStream is)
         throws UnsupportedEncodingException {

      BufferedReader reader = new BufferedReader(new InputStreamReader(is,
            HTTP.UTF_8));
      StringBuilder sb = new StringBuilder();

      String line = null;
      try {
         while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            is.close();

         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return sb.toString();
   }

   /*
    * Checks whether the device has internet connection. If not, returns false.
    */
   public static boolean isInternetConnection() {
      boolean isOnline = false;
       Context context = ApplicationContextProvider.getContext();
      ConnectivityManager connMgnr = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo nwInfo = connMgnr.getActiveNetworkInfo();
      if (nwInfo != null && nwInfo.isConnectedOrConnecting()) {
         isOnline = true;
      }
      if (!isOnline) {
         throw new AndroidRuntimeException(ServiceHelper.NO_NETWORK_CONNECTION);
      }
      return isOnline;
   }

   public static boolean isSDCardMount() {
      return Environment.MEDIA_MOUNTED.equals(Environment
            .getExternalStorageState());
   }

   public static double calculateDistance(double fromLong, double fromLat,
         double toLong, double toLat) {
      double d2r = Math.PI / 180;
      double dLong = (toLong - fromLong) * d2r;
      double dLat = (toLat - fromLat) * d2r;
      double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
            * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      double d = 6367000 * c;
      return Math.round(d);
   }
}
