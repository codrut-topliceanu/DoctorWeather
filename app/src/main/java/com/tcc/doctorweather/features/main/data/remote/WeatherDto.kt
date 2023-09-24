package com.tcc.doctorweather.features.main.data.remote

import com.squareup.moshi.Json
import com.tcc.doctorweather.features.main.domain.Weather
import kotlin.random.Random

data class CurrentWeatherResponse(
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    @Json(name = "timezone_offset")
    val timezoneOffset: Long?,
    val current: CurrentWeather?
) {
    @Json(name = "current")
    data class CurrentWeather(
        val dt: Long?,
        val sunrise: Long?,
        val sunset: Long?,
        val temp: Double?,
        val feels_like: Double?,
        val pressure: Long?,
        val humidity: Long?,
        val uvi: Double?,
        val clouds: Long?,
        val visibility: Long?,
        val wind_speed: Double?,
        val weather: List<Weather>?,
    )

    data class Weather(
        val id: Long?,
        val main: String?,
        val description: String?,
        val icon: String?,
    )
}

fun CurrentWeatherResponse.toWeather(
    tempUnit: String,
    speedUnits: String,
    timestampFallback: Long? = null
): Weather {
    return Weather(
        id = this.current?.weather?.first()?.id ?: Random.nextLong(),
        lat = this.lat?.toString() ?: "--",
        lon = this.lon?.toString() ?: "--",
        timestamp = if(this.current?.dt != null) this.current.dt * 1000 else timestampFallback,
        timezone = this.timezone ?: "--",
        temperature = this.current?.temp,
        tempFeelsLike = this.current?.feels_like,
        temperatureUnits = tempUnit,
        atmPressure = this.current?.pressure,
        humidity = this.current?.humidity,
        cloudsPercentage = this.current?.clouds,
        uvIndex = this.current?.uvi,
        windSpeed = this.current?.wind_speed,
        windSpeedUnits = speedUnits,
        currentWeather = this.current?.weather?.first()?.main ?: "",
        currentWeatherDescription = this.current?.weather?.first()?.description ?: "",
        currentWeatherIcon = this.current?.weather?.first()?.icon
    )
}