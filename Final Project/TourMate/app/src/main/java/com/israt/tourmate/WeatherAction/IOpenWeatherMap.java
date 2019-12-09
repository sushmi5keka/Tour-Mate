package com.israt.tourmate.WeatherAction;




import com.israt.tourmate.WeatherAction.CurrentWeather.WeatherResponse;
import com.israt.tourmate.WeatherAction.Weather.WeatherResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IOpenWeatherMap {

//    @GET("forecast")
//    Call<WeatherResult> getForecastWeatherByLatLang(@Query("lat") String lat,
//                                                    @Query("lat") String lng,
//                                                    @Query("lat") String appid,
//                                                    @Query("lat") String unit);


     @GET
     Call<WeatherResponse> getWeatherData1(@Url String url);




    @GET
    Call<WeatherResult> getWeatherData(@Url String url);


}
