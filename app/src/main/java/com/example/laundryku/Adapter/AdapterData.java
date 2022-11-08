package com.example.laundryku.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.laundryku.API.APIRequestData;
import com.example.laundryku.API.RetroServer;
import com.example.laundryku.Activity.MainActivity;
import com.example.laundryku.Model.DataModel;
import com.example.laundryku.Model.ResponseModel;
import com.example.laundryku.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context ctx;
    private List<DataModel> listData;
    private int idLaundry;
    private String namaLaundry, alamatLaundry, teleponLaundry, pictureLaundry;

    public AdapterData(Context ctx, List<DataModel> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvAlamat.setText(dm.getAlamat());
        holder.tvTelepon.setText(dm.getTelepon());
        holder.tvImageString.setText(dm.getPicture());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_image);
        requestOptions.error(R.drawable.ic_image);

        Glide.with(ctx)
                .load(dm.getPicture())
                .apply(requestOptions)
                .into(holder.civItem);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView tvId, tvNama, tvAlamat, tvTelepon, tvImageString;
        CircleImageView civItem;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvTelepon = itemView.findViewById(R.id.tv_telepon);
            tvImageString = itemView.findViewById(R.id.tv_imageString);
            civItem = itemView.findViewById(R.id.civ_item);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih Operasi yang Akan Dilakukan");
                    //dialogPesan.setIcon(R.mipmap.ic_launcher);
                    dialogPesan.setCancelable(true);

                    idLaundry = Integer.parseInt(tvId.getText().toString());
                    namaLaundry = tvNama.getText().toString();
                    alamatLaundry = tvAlamat.getText().toString();
                    teleponLaundry = tvTelepon.getText().toString();
                    pictureLaundry = tvImageString.getText().toString();

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteData();
//                            dialog.dismiss();
//                            ((MainActivity) ctx).retrieveData();
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ((MainActivity) ctx).fillUpdate(idLaundry, namaLaundry, alamatLaundry, teleponLaundry, pictureLaundry);
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });
        }

        private void deleteData(){
            AlertDialog.Builder dialogKofirmasiHapus = new AlertDialog.Builder(ctx);
            dialogKofirmasiHapus.setMessage("Yakin Hapus Data Ini?");

            dialogKofirmasiHapus.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
                    Call<ResponseModel> hapusData = ardData.ardDeleteData(idLaundry);

                    hapusData.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            int kode = response.body().getKode();
                            String pesan = response.body().getPesan();

                            Toast.makeText(ctx, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(ctx, "Gagal Menghubungi Server | " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.dismiss();
                    ((MainActivity) ctx).retrieveData();
                }
            });

            dialogKofirmasiHapus.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogKofirmasiHapus.show();
        }
    }
}
