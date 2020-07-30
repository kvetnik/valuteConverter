package com.example.valuteconverter;

import com.example.valuteconverter.ValCursFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ValuteApi {
    @GET("/scripts/XML_daily.asp")
    Call<ValCursFeed> getValuteCurs(@Query("date_req") String date);
}
