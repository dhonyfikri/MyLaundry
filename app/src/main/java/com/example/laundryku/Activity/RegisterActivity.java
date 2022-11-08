package com.example.laundryku.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laundryku.API.APIRequestData;
import com.example.laundryku.API.RetroServer;
import com.example.laundryku.Model.DataModelLogin;
import com.example.laundryku.Model.DataModelUser;
import com.example.laundryku.Model.ResponseModel;
import com.example.laundryku.Model.ResponseModelLogin;
import com.example.laundryku.Model.ResponseModelUser;
import com.example.laundryku.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ImageView ivRegFoto;
    private FloatingActionButton fabTakeImageReg;
    private EditText etRegUsername, etPasswordReg, etRePasswordReg, etNamaReg, etAlamatReg;
    private Button btnToLogin, btnRegister;
    private String pictureReg = "";
    private final int REQUEST_GALLERY = 9544;
    private Uri imageUri;
    private List<DataModelLogin> listDataCekReg = new ArrayList<>();
    private List<DataModelUser> listDataRegister = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ivRegFoto = findViewById(R.id.iv_regFoto);
        fabTakeImageReg = findViewById(R.id.fab_takeImageReg);
        etRegUsername = findViewById(R.id.et_regUsername);
        etPasswordReg = findViewById(R.id.et_regPassword);
        etRePasswordReg = findViewById(R.id.et_reRegPassword);
        etNamaReg = findViewById(R.id.et_regNama);
        etAlamatReg = findViewById(R.id.et_regAlamat);
        btnToLogin = findViewById(R.id.btn_toLogin);
        btnRegister = findViewById(R.id.btn_Register);

        fabTakeImageReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Open Galery"), REQUEST_GALLERY);
            }
        });

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etRegUsername.getText().toString().equals("")){etRegUsername.setError("Belum Diisi!");}
                if(etPasswordReg.getText().toString().equals("")){etPasswordReg.setError("Belum Diisi!");}
                if(etRePasswordReg.getText().toString().equals("")){etRePasswordReg.setError("Belum Diisi!");}
                if(etNamaReg.getText().toString().equals("")){etNamaReg.setError("Belum Diisi!");}
                if(etAlamatReg.getText().toString().equals("")){etAlamatReg.setError("Belum Diisi!");}
                if(etPasswordReg.getText().toString().equals(etRePasswordReg.getText().toString())){
                    if(!etPasswordReg.getText().toString().equals("")&&!etRePasswordReg.getText().toString().equals("")&&!etRegUsername.getText().toString().equals("")&&!etNamaReg.getText().toString().equals("")&&!etAlamatReg.getText().toString().equals("")){
                        validasiRegister();
                    }
                }
                else {
                    etRePasswordReg.setError("Password Tidak Match!");
                    Toast.makeText(RegisterActivity.this, "Password Not Match...!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                ivRegFoto.setImageBitmap(bitmap);

                pictureReg = getStringImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void validasiRegister(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModelLogin> validatingRegister = ardData.ardCekRegister(etRegUsername.getText().toString(), "none", "cek-duplikat");

        validatingRegister.enqueue(new Callback<ResponseModelLogin>() {
            @Override
            public void onResponse(Call<ResponseModelLogin> call, Response<ResponseModelLogin> response) {
                listDataCekReg = response.body().getDataLogin(0);
                DataModelLogin dml = listDataCekReg.get(0);
                int ff = Integer.parseInt(dml.getUsercount());

                if(ff == 0){
                    register();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Username Sudah Terdaftar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModelLogin> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "(Cek) Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModelUser> registerUser = ardData.ardRegister(etRegUsername.getText().toString(), etPasswordReg.getText().toString(), etNamaReg.getText().toString(), etAlamatReg.getText().toString(), pictureReg);

        registerUser.enqueue(new Callback<ResponseModelUser>() {
            @Override
            public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
                progressDialog.dismiss();
                int kode = response.body().getKodeUser();
                String pesan = response.body().getPesanUser();

                Toast.makeText(RegisterActivity.this, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModelUser> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "(create) Gagal Menghubungi Server Saat Register | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}