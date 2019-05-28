package com.example.mellofood;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface JsonPlaceHolderApi {
    @GET("catalog/outlet?lang_code=en_US")
    Call<JSONObject> getOutlet(@HeaderMap Map<String,String> headers);
}
