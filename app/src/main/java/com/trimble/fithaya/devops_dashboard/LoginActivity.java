package com.trimble.fithaya.devops_dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

////added comment
public class LoginActivity extends Activity {
    private EditText txtUserName;
    private EditText txtPassword;
    private Button btnSignin;
    private TextView lblStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUserName = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);
        btnSignin = (Button) findViewById(R.id.sign_in_button);
        //lblStatus = (TextView) findViewById(R.id.login_status);
        btnSignin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                try {
                    if (username.equalsIgnoreCase("USER") && password.equals("12345")) {
                        //lblResult.setText("Login successful");
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(loginIntent);
                    } else {

                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}