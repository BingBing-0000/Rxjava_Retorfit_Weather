package com.example.rxjava_retorfit_weather;


//import io.reactivex.rxjava3.core.Observable 需要用Rxjava3的版本 不要import錯
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyAPIWeatherService {
    //拿取後段的網址 以'?'為界線
    @GET("api/v1/rest/datastore/F-C0032-001")
    Observable<Weather_Respone> getWeater(
            @Query("Authorization") String authorization
    );

}
