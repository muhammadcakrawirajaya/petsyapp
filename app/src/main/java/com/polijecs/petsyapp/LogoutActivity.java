package com.polijecs.petsyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.polijecs.petsyapp.databinding.ActivityLogoutBinding;

public class LogoutActivity extends AppCompatActivity {
    private ActivityLogoutBinding logoutBinding;
    private AppConfig appConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_logout);
        logoutBinding = DataBindingUtil.setContentView(this,R.layout.activity_logout);
        String name = getIntent().getStringExtra("name");
        appConfig = new AppConfig(this);
        logoutBinding.txtUserwelcome.setText("Welcome "+ name);
        logoutBinding.bnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appConfig.updateUserLoginStatus(false);
                startActivity(new Intent(LogoutActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}