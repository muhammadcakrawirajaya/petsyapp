package com.polijecs.petsyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.polijecs.petsyapp.databinding.ActivityRegisterBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding registerBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setSupportActionBar(registerBinding.myToolbar);
        getSupportActionBar().setTitle("Sign Up");
        registerBinding.bnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp();
                registerBinding.showProgress.setVisibility(View.VISIBLE);
            }
        });
    }
    private void performSignUp(){
        String userName = registerBinding.txtUserName.getText().toString();
        String password = registerBinding.txtUserPassword.getText().toString();
        String name = registerBinding.txtName.getText().toString();
        Call<ApiResponse> call = ApiClient.getApiClient().create(ApiInterface.class).performUserSignIn(userName,password,name);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.code()==200)
                {
                    if(response.body().getStatus().equals("ok"))
                    {
                        if(response.body().getResultCode()==1)
                        {
                            Toast.makeText(RegisterActivity.this, "Registration Success",Toast.LENGTH_LONG).show();
                            onBackPressed();
                            finish();
                        } else
                        {
                            displayUserInfo("User Already Exists");
                            registerBinding.txtUserPassword.setText("");
                        }
                    } else
                    {
                        displayUserInfo("Something Went Wrong...");
                        registerBinding.txtUserPassword.setText("");
                    }
                }
                else
                {
                    displayUserInfo("Something Went Wrong...");
                    registerBinding.txtUserPassword.setText("");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }
    private void displayUserInfo(String message){
        Snackbar.make(registerBinding.myCoordinatorLayout,message,Snackbar.LENGTH_SHORT).show();
        registerBinding.txtUserPassword.setText("");
        registerBinding.showProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}