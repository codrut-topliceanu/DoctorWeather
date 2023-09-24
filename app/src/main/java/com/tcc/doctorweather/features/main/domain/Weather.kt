package com.tcc.doctorweather.features.main.domain


data class Weather(
    val id: Long = 0,
    val lat: String = "--",
    val lon: String = "--",
    val timezone: String = "--",
    val timestamp: Long? = null,
    val temperature: Double? = null,
    val tempFeelsLike: Double? = null,
    val temperatureUnits: String = "--", //Kelvin Celsius	Fahrenheit
    val atmPressure: Long? = null, // hPa
    val humidity: Long? = null, // %
    val cloudsPercentage: Long? = null, // %
    val uvIndex: Double? = null,
    val windSpeed: Double? = null, // metre/sec metre/sec	miles/hour
    val windSpeedUnits: String = "--",
    val currentWeather: String = "--", // rain, snow, clear, etc
    val currentWeatherDescription: String = "--",
    val currentWeatherIcon: String? = null
)