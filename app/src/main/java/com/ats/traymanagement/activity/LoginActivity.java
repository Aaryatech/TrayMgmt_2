package com.ats.traymanagement.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.constants.Constants;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edUsername, edPassword;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername = findViewById(R.id.edLogin_UserName);
        edPassword = findViewById(R.id.edLogin_Password);
        tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tvLogin) {
            if (edUsername.getText().toString().isEmpty()) {
                edUsername.setError("Required");
                edUsername.requestFocus();
            } else if (edPassword.getText().toString().isEmpty()) {
                edPassword.setError("Required");
                edPassword.requestFocus();
            } else {

                String username = edUsername.getText().toString();
                String pass = edPassword.getText().toString();

                if (username.equals("gfpl") && pass.equals("123")) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", "gfpl");
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
