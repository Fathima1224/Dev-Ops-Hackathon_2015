/**
 * Copyright Trimble Inc., 2013 - 2014 All rights reserved.
 * 
 * Licensed Software Confidential and Proprietary Information of Trimble Inc.,
 * made available under Non-Disclosure Agreement OR License as applicable.
 * 
 * Product Name: ${Supervisor}
 * 
 * Module Name: com.trimble.supervisor.common
 * 
 * File name: ${Log.java}
 * 
 * Author: ${Deepika}
 * 
 * Created On: ${25/04/14}
 * 
 * Abstract:
 * All java classes can make use of this class and
 * print the log messages of debug/info/error type.
 * 
 * Environment: Mobile Profile :Android
 * 
 * 
 * Notes:
 * 
 * Revision History:
 * 
 * 
 */
package com.trimble.fsm.fieldmaster.service.common;

public class Log {

   public static Log       logger           = null;

   public static final int LOG_TYPE_DEBUG   = 0;
   public static final int LOG_TYPE_INFO    = 1;
   public static final int LOG_TYPE_ERROR   = 2;
   public static final int LOG_TYPE_VERBOSE = 3;
   public static final int LOG_TYPE_WARNING = 4;


   /**
    * This method calls android log function and
    * prints the log messages to logcat.
    * 
    */

   public static void printLog(boolean isLogger, int iType, String stTag,
         String stMsg) {

      if (isLogger) {

         switch (iType) {
            case LOG_TYPE_DEBUG:
               android.util.Log.d(stTag, stMsg);
               break;
            case LOG_TYPE_INFO:
               android.util.Log.i(stTag, stMsg);
               break;
            case LOG_TYPE_ERROR:
               android.util.Log.e(stTag, stMsg);
               break;
            case LOG_TYPE_VERBOSE:
               android.util.Log.v(stTag, stMsg);
               break;
            case LOG_TYPE_WARNING:
               android.util.Log.w(stTag, stMsg);
               break;

            default:
               break;
         }
      }

   }

}
