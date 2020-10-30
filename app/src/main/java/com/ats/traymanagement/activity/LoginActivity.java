package com.ats.traymanagement.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.traymanagement.R;
import com.ats.traymanagement.common.CommonDialog;
import com.ats.traymanagement.constants.Constants;
import com.ats.traymanagement.model.LoginModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

                if (username.equals("admin") && pass.equals("123")) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", "GFPL ADMIN");
                    editor.putInt("isAdmin", 1);
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }else if (username.equals("security") && pass.equals("security")) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", "GFPL SECURITY");
                    editor.putInt("isAdmin", 0);
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
