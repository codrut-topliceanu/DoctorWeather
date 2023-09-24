package com.tcc.doctorweather.features.main.domain

import com.tcc.doctorweather.features.main.data.repository.MeasurementUnits
import com.tcc.doctorweather.utils.RepoResult

interface WeatherRepository {

    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        units: MeasurementUnits = MeasurementUnits.METRIC,
        language: String = "en",
    ): RepoResult<Weather>
}