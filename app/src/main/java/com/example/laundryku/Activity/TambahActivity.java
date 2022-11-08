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
import com.example.laundryku.Model.ResponseModel;
import com.example.laundryku.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {

    private EditText etNama, etAlamat, etTelepon;
    private Button btnSimpan;
    private String nama, alamat, telepon, picture = "";
    private final int REQUEST_GALLERY = 9544;
    private Uri imageUri;
    private FloatingActionButton fabPickImage, fabDeleteImage;
    private ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etTelepon = findViewById(R.id.et_telepon);
        btnSimpan = findViewById(R.id.btn_simpan);
        fabPickImage = findViewById(R.id.fab_pickImage);
        fabDeleteImage = findViewById(R.id.fab_deleteImage);
        ivPicture = findViewById(R.id.iv_picture);

        fabDeleteImage.setVisibility(View.INVISIBLE);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = etNama.getText().toString();
                alamat = etAlamat.getText().toString();
                telepon = etTelepon.getText().toString();

                if(nama.trim().equals("")){
                    etNama.setError("Nama harus diisi");
                }
                if(alamat.trim().equals("")){
                    etAlamat.setError("Alamat harus diisi");
                }
                if(telepon.trim().equals("")){
                    etTelepon.setError("Telepon harus diisi");
                }
                if(!nama.trim().equals("") && !alamat.trim().equals("") && !telepon.trim().equals("")){
                    createData();
                }
            }
        });

        fabPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Open Galery"), REQUEST_GALLERY);
            }
        });

        fabDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_image));
                picture = "";
                fabDeleteImage.setVisibility(View.INVISIBLE);
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

                ivPicture.setImageBitmap(bitmap);

                picture = getStringImage(bitmap);

                fabDeleteImage.setVisibility(View.VISIBLE);

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

    private void createData(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> simpanlData = ardData.ardCreateData(nama, alamat, telepon, picture);

        simpanlData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TambahActivity.this, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}