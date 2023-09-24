package com.tcc.doctorweather.features.main.ui

import com.tcc.doctorweather.features.main.data.repository.MeasurementUnits
import com.tcc.doctorweather.features.main.domain.Weather

/**
 * The main UI state of the [WeatherScreen].
 * @param inProgress used to signal that a request is in progress
 * @param currentWeather currently displayed weather
 */
data class WeatherScreenUiState(
    val inProgress: Boolean = false,
    val currentWeather: Weather,
    val selectedMeasurementUnits: MeasurementUnits = MeasurementUnits.METRIC,
    val searchQuery: String = ""
)

/**
 * Represents a stateAction that modifies the UI state in some way.
 */
interface StateAction<T : Any> {
    fun updateState(state: T): T
}


/**
 * This is a list of actions available to be taken to update the UI state.
 */
sealed class WeatherScreenAction : StateAction<WeatherScreenUiState> {

    data class UpdateInProgress(val showLoading: Boolean) : WeatherScreenAction() {
        override fun updateState(state: WeatherScreenUiState): WeatherScreenUiState =
            state.copy(inProgress = showLoading)
    }

    data class UpdateCurrentWeather(val weather: Weather) : WeatherScreenAction() {
        override fun updateState(state: WeatherScreenUiState): WeatherScreenUiState =
            state.copy(currentWeather = weather)
    }

    data class UpdateMeasurementUnits(val units: MeasurementUnits) : WeatherScreenAction() {
        override fun updateState(state: WeatherScreenUiState): WeatherScreenUiState =
            state.copy(selectedMeasurementUnits = units)
    }
}