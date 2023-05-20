package com.polijecs.petsyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.material.snackbar.Snackbar;
import com.polijecs.petsyapp.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private boolean isRememberUserLogin = false;
    private AppConfig appConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mainBinding.myToolbar);
        getSupportActionBar().setTitle("Petsy");
        appConfig = new AppConfig(this);
        if(appConfig.isUserLogin()){
            String name = appConfig.getNameOfUser();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();
        }
        mainBinding.bnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        mainBinding.bnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
                mainBinding.showProgress.setVisibility(View.VISIBLE);
            }
        });
    }
    private void performLogin(){
        String userName = mainBinding.txtUserName.getText().toString();
        String password = mainBinding.txtUserPassword.getText().toString();
        Call<ApiResponse> call = ApiClient.getApiClient().create(ApiInterface.class).performUserLogin(userName,password);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.code()==200){
                    if (response.body().getStatus().equals("ok")){
                        if(response.body().getResultCode()==1){
                            String name = response.body().getName();
                            if(isRememberUserLogin){
                                appConfig.updateUserLoginStatus(true);
                                appConfig.saveNameOfUser(name);
                            }
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("name", name);
                            startActivity(intent);
                            finish();
                        } else {
                            displayUserInformation("Login Failed");
                            mainBinding.txtUserPassword.setText("");
                        }
                    } else {
                        displayUserInformation("Something Went Wrong");
                        mainBinding.txtUserPassword.setText("");
                    }
                }else {
                    displayUserInformation("Something Went Wrong");
                    mainBinding.txtUserPassword.setText("");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
    private void displayUserInformation(String message){
        Snackbar.make(mainBinding.myCoordinatorLayout,message,Snackbar.LENGTH_SHORT).show();
        mainBinding.showProgress.setVisibility(View.INVISIBLE);
    }

    public void checkBoxClicked(View view){
        isRememberUserLogin = ((CheckBox)view).isChecked();
    }
}