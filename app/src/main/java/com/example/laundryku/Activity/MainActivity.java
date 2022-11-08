package com.example.laundryku.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundryku.API.APIRequestData;
import com.example.laundryku.API.RetroServer;
import com.example.laundryku.Adapter.AdapterData;
import com.example.laundryku.Model.DataModel;
import com.example.laundryku.Model.DataModelLogin;
import com.example.laundryku.Model.DataModelUser;
import com.example.laundryku.Model.ResponseModel;
import com.example.laundryku.Model.ResponseModelUser;
import com.example.laundryku.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<DataModel> listData = new ArrayList<>();
    private SwipeRefreshLayout srlData;
    private ProgressBar pbData;
    private TextView txtKosong;
    private FloatingActionButton fabTambah, fabAkun;
    private String LoginAsUsername, idUser, passwordUser, usernameUser, namaUser, alamatUser, fotoUser;
    private List<DataModelUser> listDataUser = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        LoginAsUsername = extras.getString("USER");

        rvData = findViewById(R.id.rv_data);
        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);

        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);
        txtKosong = findViewById(R.id.txt_Kosong);
        fabTambah = findViewById(R.id.fab_tambah);
        fabAkun = findViewById(R.id.fab_akun);

        //retrieveData();

        srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlData.setRefreshing(true);
                retrieveData();
                srlData.setRefreshing(false);
            }
        });

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TambahActivity.class));
            }
        });

        fabAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
                Call<ResponseModelUser> getUserData = ardData.ardRetrieveUser(LoginAsUsername);

                getUserData.enqueue(new Callback<ResponseModelUser>() {
                    @Override
                    public void onResponse(Call<ResponseModelUser> call, Response<ResponseModelUser> response) {
                        int kode = response.body().getKodeUser();
                        String pesan = response.body().getPesanUser();

                        if(kode == 1){
                            listDataUser = response.body().getDataUser();
                            DataModelUser dmu = listDataUser.get(0);
                            idUser = Integer.toString(dmu.getId_user());
                            usernameUser = dmu.getUsername();
                            passwordUser = dmu.getPassword_user();
                            namaUser = dmu.getNama_user();
                            alamatUser = dmu.getAlamat_user();
                            fotoUser = dmu.getFoto_user();

                            try{
                                Intent intent = new Intent(MainActivity.this, AkunActivity.class);
                                intent.putExtra("ID", idUser);
                                intent.putExtra("USERNAME", namaUser);
                                intent.putExtra("PASSWORD", passwordUser);
                                intent.putExtra("NAMA", namaUser);
                                intent.putExtra("ALAMAT", alamatUser);
                                intent.putExtra("FOTO", fotoUser);
                                startActivity(intent);

                                Toast.makeText(MainActivity.this, "Fetch Data User Sukses", Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getApplication(), "Error, Coba Lagi...", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Gagal Mengambil Data User | Kode : " + kode + " Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModelUser> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }

    public void retrieveData(){
        pbData.setVisibility(View.VISIBLE);

        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilData = ardData.ardRetrieveData();

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                if(kode == 1){
                    rvData.setVisibility(View.VISIBLE);
                    txtKosong.setVisibility(View.GONE);

                    //Toast.makeText(MainActivity.this, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                    listData = response.body().getData();

                    adData = new AdapterData(MainActivity.this, listData);
                    rvData.setAdapter(adData);
                    adData.notifyDataSetChanged();
                }
                else {
                    rvData.setVisibility(View.GONE);
                    txtKosong.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        pbData.setVisibility(View.INVISIBLE);
    }

    public void fillUpdate(int id, String nama, String alamat, String telepon, String picture){
        try{
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
            intent.putExtra("ID", id);
            intent.putExtra("NAMA", nama);
            intent.putExtra("ALAMAT", alamat);
            intent.putExtra("TELEPON", telepon);
            intent.putExtra("PICTURE", picture);
            startActivity(intent);

        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplication(), "Error, Coba Lagi...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title dialog
        alertDialogBuilder.setTitle("Konfirmasi Keluar");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah Yakin Untuk Logout?")
                .setIcon(R.mipmap.logo_kumbahku)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}