package com.example.systemcameraapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    File file=new File("");
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImIyMzUxNzVjLWY2ZjktNDQ3Ny1hZTI1LTFmMDRkY2NhYmNmZSJ9.iGAixDt3O35mz_UyCYHGYwnGUFXeNF46z3TgsgeFezKOzBEr5ydLJILkQFEEkpTBEHLtwQrF_OmNJKKhg09MzQ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.23.91:10002")
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }else{
                    startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), 2);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiService.UserUpData(token,new MultipartBody.Builder()
                        .addFormDataPart("userId","29")
                        .addFormDataPart("phonenumber","15806155815")
                        .addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("multipart/form-data"), file))
                        .build()).enqueue(new Callback<ReturnOK>() {
                    @Override
                    public void onResponse(Call<ReturnOK> call, Response<ReturnOK> response) {
                        ReturnOK returnOK = response.body();
                        Log.d("TAG", "onResponse: "+returnOK.getMsg());
                    }

                    @Override
                    public void onFailure(Call<ReturnOK> call, Throwable throwable) {
                        Log.d("TAG", "onFailure: ");
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==2&&resultCode==RESULT_OK)
        {
            imageView.setImageURI(data.getData());
            Cursor cursor=getContentResolver().query(data.getData(),null,null,null,null);
            if (cursor!=null){
                cursor.moveToFirst();
                file=new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));

            }
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}