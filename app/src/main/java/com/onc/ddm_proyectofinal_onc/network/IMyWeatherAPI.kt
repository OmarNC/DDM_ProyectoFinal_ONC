package com.onc.ddm_proyectofinal_onc.network

import com.onc.ddm_proyectofinal_onc.model.WeatherNow
import com.onc.ddm_proyectofinal_onc.model.WeatherPollutionToday
import com.onc.ddm_proyectofinal_onc.model.WeatherForecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface IMyWeatherAPI {
    @GET
    fun getCurrentWeather(
        @Url url: String?,
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") APIkey: String?,
        @Query("lang") lang: String?,
        @Query("units") units: String?
        //"https://api.openweathermap.org/data/2.5/weather?lang=se&units=metric&lat={lat}&lon={lon}&appid={APIkey}"
    ): Call<WeatherNow>

    @GET
    fun getForecastWeather(
        @Url url: String?,
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") APIkey: String?,
        @Query("lang") lang: String?,
        @Query("units") units: String?
        //api.openweathermap.org/data/2.5/forecast?lang=se&units=metric&lat={lat}&lon={lon}&appid={APIkey}
    ): Call<WeatherForecast>

    @GET
    fun getCurrentPollution(
        @Url url: String?,
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") APIkey: String?
        //http://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={API key}
    ): Call<WeatherPollutionToday>

    @GET
    fun getForecastPollution(
        @Url url: String?,
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") APIkey: String?
        //"http://api.openweathermap.org/data/2.5/air_pollution/forecast?lat={lat}&lon={lon}&appid={API key}"
    ): Call<WeatherPollutionToday>



    /*
        @GET("location_detail.php")  //La geneeración de la rita sera: location_detail.php?id=3523272
    fun getLocationDetail(
        @Url url: String?,
        @Query("id") id: Int?
    ): Call<ArrayList<LocationDetail>>



    @GET("myweather/location_detail/{id}")
    fun getLocationDetail(
        @Path("id") id: String?
    ): Call<LocationDetail>

     */
}