package com.example.systemcameraapplication;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {
    @POST("/system/user/updata")
    Call<ReturnOK> UserUpData(@Header("Authorization") String Authorization, @Body RequestBody body);
}
