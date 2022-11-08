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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.laundryku.API.APIRequestData;
import com.example.laundryku.API.RetroServer;
import com.example.laundryku.Model.DataModel;
import com.example.laundryku.Model.ResponseModel;
import com.example.laundryku.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    private TextView tvIdUp;
    private EditText etNamaUp, etAlamatUp, etTeleponUp;
    private Button btnUpdate, btnBatalUpdate;
    private ImageView ivImageUp;
    private int idUp;
    private String namaUp, alamatUp, teleponUp, stringActualImage, pictureUp = "";
    private final int REQUEST_GALLERY = 9544;
    private Uri imageUri;
    private ImageView ivPicture;
    private FloatingActionButton fabPickImageUp, fabDeleteImageUp, fabReturnImageUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        tvIdUp = findViewById(R.id.tv_idUp);
        etNamaUp = findViewById(R.id.et_namaUp);
        etAlamatUp = findViewById(R.id.et_alamatUp);
        etTeleponUp = findViewById(R.id.et_teleponUp);
        ivImageUp = findViewById(R.id.iv_pictureUp);
        btnUpdate = findViewById(R.id.btn_update);
        btnBatalUpdate = findViewById(R.id.btn_batalUpdate);
        fabPickImageUp = findViewById(R.id.fab_pickImageUp);
        fabDeleteImageUp = findViewById(R.id.fab_deleteImageUp);
        fabReturnImageUp = findViewById(R.id.fab_returnImageUp);

        fabReturnImageUp.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        tvIdUp.setText(String.valueOf(extras.getInt("ID")));
        etNamaUp.setText(extras.getString("NAMA"));
        etAlamatUp.setText(extras.getString("ALAMAT"));
        etTeleponUp.setText(extras.getString("TELEPON"));
        stringActualImage = extras.getString("PICTURE");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_image);
        requestOptions.error(R.drawable.ic_image);

        Glide.with(this)
                .load(stringActualImage)
                .apply(requestOptions)
                .into(ivImageUp);

        fabPickImageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Open Galery"), REQUEST_GALLERY);
            }
        });

        btnBatalUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idUp = Integer.parseInt(tvIdUp.getText().toString());
                namaUp = etNamaUp.getText().toString();
                alamatUp = etAlamatUp.getText().toString();
                teleponUp = etTeleponUp.getText().toString();

                if(namaUp.trim().equals("")){
                    etNamaUp.setError("Nama harus diisi");
                }
                if(alamatUp.trim().equals("")){
                    etAlamatUp.setError("Alamat harus diisi");
                }
                if(teleponUp.trim().equals("")){
                    etTeleponUp.setError("Telepon harus diisi");
                }
                if(!namaUp.trim().equals("") && !alamatUp.trim().equals("") && !teleponUp.trim().equals("")){
                    perbaruiData();
                }
            }
        });

        fabDeleteImageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImageUp.setImageDrawable(getResources().getDrawable(R.drawable.ic_image));
                pictureUp = "hapusfoto";
                fabDeleteImageUp.setVisibility(View.INVISIBLE);
                fabReturnImageUp.setVisibility(View.VISIBLE);
            }
        });

        fabReturnImageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureUp = "";
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.skipMemoryCache(true);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.placeholder(R.mipmap.ic_launcher);
                requestOptions.error(R.mipmap.ic_launcher);

                Glide.with(UpdateActivity.this)
                        .load(stringActualImage)
                        .apply(requestOptions)
                        .into(ivImageUp);
                fabReturnImageUp.setVisibility(View.INVISIBLE);
                fabDeleteImageUp.setVisibility(View.VISIBLE);
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

                ivImageUp.setImageBitmap(bitmap);

                pictureUp = getStringImage(bitmap);

                fabDeleteImageUp.setVisibility(View.VISIBLE);

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

    private void perbaruiData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> updateData = ardData.ardUpdateData(idUp, namaUp, alamatUp, teleponUp, pictureUp);

        updateData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UpdateActivity.this, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdateActivity.this, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}