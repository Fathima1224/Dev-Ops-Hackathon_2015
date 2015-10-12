package com.trimble.fithaya.devops_dashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {
    private EditText txtUserName = (EditText) findViewById(R.id.username);
    private EditText txtPassword = (EditText) findViewById(R.id.password);
    private Button btnSignin = (Button) findViewById(R.id.sign_in_button);

   btnSignin.setOnClickListener (new OnClickListener() {


     //   btnSignin.setOnClickListener(new View.OnClickListener() {

            @Override
        public void onClick (View v){
            String username = txtUserName.getText().toString();
            String password = txtPassword.getText().toString();
            try {
                if (username.length() > 0 && password.length() > 0) {
                    if (username.equals("admin") && password.equals("admin123")) {


                    }
                }
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


    });

}