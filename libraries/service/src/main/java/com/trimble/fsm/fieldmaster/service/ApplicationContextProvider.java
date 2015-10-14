package com.trimble.fsm.fieldmaster.service;

import android.content.Context;

public class ApplicationContextProvider {

    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;

    public static void setContext(Context sContext) {
        ApplicationContextProvider.sContext = sContext;
    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }

}