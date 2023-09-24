package com.tcc.doctorweather.features.main.data.remote

import com.tcc.doctorweather.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {

    @Headers("Content-Type: application/json")
    @GET("onecall")
    suspend fun getWeatherAtLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,hourly,daily,alerts",
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): Response<CurrentWeatherResponse>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}