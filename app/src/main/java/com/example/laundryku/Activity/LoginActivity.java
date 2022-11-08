package com.example.laundryku.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laundryku.API.APIRequestData;
import com.example.laundryku.API.RetroServer;
import com.example.laundryku.Adapter.AdapterData;
import com.example.laundryku.Model.DataModel;
import com.example.laundryku.Model.DataModelLogin;
import com.example.laundryku.Model.ResponseModel;
import com.example.laundryku.Model.ResponseModelLogin;
import com.example.laundryku.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnToRegister;
    private EditText etUsername, etPassword;
    String username, password_user;
    private List<DataModelLogin> listDataLogin = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        btnToRegister = findViewById(R.id.btn_toRegister);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUsername.getText().toString().equals("")){
                    etUsername.setError("Username harus diisi");
                }
                if(etPassword.getText().toString().equals("")){
                    etPassword.setError("Password harus diisi");
                }
                if(!etUsername.getText().toString().equals("")&&!etPassword.getText().toString().equals("")){
                    masuk();
                }
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void masuk(){
        username = etUsername.getText().toString();
        password_user = etPassword.getText().toString();

        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModelLogin> requestLogin = ardData.ardRetrieveLogin(username, password_user, "cek-login");

        requestLogin.enqueue(new Callback<ResponseModelLogin>() {
            @Override
            public void onResponse(Call<ResponseModelLogin> call, Response<ResponseModelLogin> response) {

                listDataLogin = response.body().getDataLogin(0);
                DataModelLogin dml = listDataLogin.get(0);
                int ff = Integer.parseInt(dml.getUsercount());

                if(ff > 0){
                    try{
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USER", etUsername.getText().toString());
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplication(), "Error, Coba Lagi...", Toast.LENGTH_SHORT).show();
                    }

                    etUsername.setText("");
                    etPassword.setText("");
                }
                else {
                    Toast.makeText(LoginActivity.this, "Username / Password Salah...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModelLogin> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}