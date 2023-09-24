package com.tcc.doctorweather.features.main.data.repository

import android.util.Log
import com.tcc.doctorweather.features.main.data.remote.WeatherApi
import com.tcc.doctorweather.features.main.data.remote.toWeather
import com.tcc.doctorweather.features.main.domain.Weather
import com.tcc.doctorweather.features.main.domain.WeatherRepository
import com.tcc.doctorweather.utils.RepoResult
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        units: MeasurementUnits,
        language: String
    ): RepoResult<Weather> {
        try {

            val response = weatherApi.getWeatherAtLocation(
                lat = lat,
                lon = lon,
                units = units.unitName,
                language = language
            )

            Log.i("WeatherRepositoryImpl", "getCurrentWeather: ${response.body()}")

            if (!response.isSuccessful || response.body() == null) {
                Log.e("WeatherRepositoryImpl", "getCurrentWeather error : ${response.errorBody()}")
                return RepoResult.Error("Called failed, response unsuccessful.")
            }

            val result =
                response.body()?.toWeather(
                    tempUnit = units.tempUnit,
                    speedUnits = units.speedUnits,
                    timestampFallback = System.currentTimeMillis()
                )

            return RepoResult.Success(result)

        } catch (e: Exception) {
            Log.e("WeatherRepositoryImpl", "getCurrentWeather exception:", e)
            return RepoResult.Error("Called failed, response unsuccessful.")
        }
    }
}

enum class MeasurementUnits(val unitName: String, val tempUnit: String, val speedUnits: String) {
    STANDARD("standard", "Kelvin", "metre/sec"),
    METRIC("metric", "Celsius", "metre/sec"),
    IMPERIAL("imperial", "Fahrenheit", "miles/hour")
}

