package com.example.laundryku.API;

import com.example.laundryku.Model.ResponseModel;
import com.example.laundryku.Model.ResponseModelLogin;
import com.example.laundryku.Model.ResponseModelUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequestData {
    @GET("retrieve.php")
    Call<ResponseModel> ardRetrieveData();

    @FormUrlEncoded
    @POST("create.php")
    Call<ResponseModel> ardCreateData(
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telepon") String telepon,
            @Field("picture") String picture
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseModel> ardDeleteData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseModel> ardUpdateData(
            @Field("id") int id,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telepon") String telepon,
            @Field("picture") String picture
    );

    @FormUrlEncoded
    @POST("retrieve_login.php")
    Call<ResponseModelLogin> ardRetrieveLogin(
            @Field("username") String username,
            @Field("password_user") String password_user,
            @Field("metode") String metode
    );

    @FormUrlEncoded
    @POST("retrieve_login.php")
    Call<ResponseModelLogin> ardCekRegister(
            @Field("username") String username,
            @Field("password_user") String password_user,
            @Field("metode") String metode
    );

    @FormUrlEncoded
    @POST("create_user.php")
    Call<ResponseModelUser> ardRegister(
            @Field("username") String username,
            @Field("password_user") String metode,
            @Field("nama_user") String nama_user,
            @Field("alamat_user") String alamat_user,
            @Field("foto_user") String foto_user
    );

    @FormUrlEncoded
    @POST("retrieve_user.php")
    Call<ResponseModelUser> ardRetrieveUser(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("update_user.php")
    Call<ResponseModelUser> ardUpdateUser(
            @Field("id_user") String id_user,
            @Field("username") String username,
            @Field("password_user") String password_user,
            @Field("nama_user") String nama_user,
            @Field("alamat_user") String alamat_user,
            @Field("foto_user") String foto_user
    );
}
