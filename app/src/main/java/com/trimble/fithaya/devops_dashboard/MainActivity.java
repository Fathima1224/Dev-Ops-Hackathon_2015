package com.trimble.fithaya.devops_dashboard;

<<<<<<< HEAD
//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
=======
import android.support.v7.app.ActionBarActivity;
>>>>>>> 36e97d7082c43833abf20376df79ca1c6878522f
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

<<<<<<< HEAD
public class MainActivity extends Activity {
=======
import com.trimble.oculus.core.mobile.auth.DelegateAuthAPI;

public class MainActivity extends ActionBarActivity {
>>>>>>> 36e97d7082c43833abf20376df79ca1c6878522f

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DelegateAuthAPI.getInstance("RETURN").authenticateUser("a","a");
    }
}
