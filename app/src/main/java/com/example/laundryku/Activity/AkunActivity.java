package com.example.laundryku.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.laundryku.API.APIRequestData;
import com.example.laundryku.API.RetroServer;
import com.example.laundryku.Model.ResponseModel;
import com.example.laundryku.Model.ResponseModelUser;
import com.example.laundryku.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunActivity extends AppCompatActivity {

    private String idAkun, passwordAkun, usernameAkun, namaAkun, alamatAkun, fotoAkun, acountPicture = "";
    private ImageView ivPictureAkun;
    private FloatingActionButton fabPickImageAkun;
    private EditText etNamaAkun, etAlamatAkun, etKonfirmasiPassword;
    private Button btnCancelUpdateAkun, btnUpdateAkun;
    private final int REQUEST_GALLERY = 9544;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);

        Bundle extras = getIntent().getExtras();
        idAkun = extras.getString("ID");
        passwordAkun = extras.getString("PASSWORD");
        usernameAkun = extras.getString("USERNAME");
        namaAkun = extras.getString("NAMA");
        alamatAkun = extras.getString("ALAMAT");
        fotoAkun = extras.getString("FOTO");

        ivPictureAkun = findViewById(R.id.iv_pictureAkun);
        fabPickImageAkun = findViewById(R.id.fab_pickImageAkun);
        etNamaAkun = findViewById(R.id.et_namaAkun);
        etAlamatAkun = findViewById(R.id.et_alamatAkun);
        etKonfirmasiPassword = findViewById(R.id.et_konfirPassword);
        btnCancelUpdateAkun = findViewById(R.id.btn_cancelUpdateAkun);
        btnUpdateAkun = findViewById(R.id.btn_updateAkun);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_blue_account);
        requestOptions.error(R.drawable.ic_blue_account);

        Glide.with(this)
                .load(fotoAkun)
                .apply(requestOptions)
                .into(ivPictureAkun);

        etNamaAkun.setText(namaAkun);
        etAlamatAkun.setText(alamatAkun);

        fabPickImageAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Open Galery"), REQUEST_GALLERY);
            }
        });

        btnCancelUpdateAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdateAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNamaAkun.getText().toString().equals("")){etNamaAkun.setError("Belum Diisi!");}
                if(etAlamatAkun.getText().toString().equals("")){etAlamatAkun.setError("Belum Diisi!");}
                if(!etNamaAkun.getText().toString().equals("")&&!etAlamatAkun.getText().toString().equals("")){
                    if(etKonfirmasiPassword.getText().toString().equals(passwordAkun)){
                        updateAkun();
                    }
                    else {
                        Toast.makeText(AkunActivity.this, "Password Anda Salah...!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AkunActivity.this, "Data Harus Lengkap!", Toast.LENGTH_SHORT).show();
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

                ivPictureAkun.setImageBitmap(bitmap);

                acountPicture = getStringImage(bitmap);

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

    private void updateAkun(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModelUser> updateDataUser = ardData.ardUpdateUser(idAkun, usernameAkun, passwordAkun, etNamaAkun.getText().toString(), etAlamatAkun.getText().toString(), acountPicture);

        updateDataUser.enqueue(new Callback<ResponseModelUser>() {
            @Override
            public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
                progressDialog.dismiss();
                int kode = response.body().getKodeUser();
                String pesan = response.body().getPesanUser();

                Toast.makeText(AkunActivity.this, "Sukses | Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModelUser> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AkunActivity.this, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}