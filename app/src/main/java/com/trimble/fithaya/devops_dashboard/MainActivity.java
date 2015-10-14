package com.trimble.fithaya.devops_dashboard;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.trimble.oculus.core.mobile.auth.DelegateAuthAPI;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DelegateAuthAPI.getInstance("RETURN").authenticateUser("a","a");
    }
}
