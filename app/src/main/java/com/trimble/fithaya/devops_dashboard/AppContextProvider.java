package com.trimble.fithaya.devops_dashboard;

import android.app.Application;
import android.content.Context;

import com.trimble.fsm.fieldmaster.service.ApplicationContextProvider;

/**
 * Created by fithaya on 14-10-2015.
 */
public class AppContextProvider extends Application {
    Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationContextProvider.setContext(getApplicationContext());
    }
}
