package com.tcc.doctorweather.features.main.ui

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcc.doctorweather.BuildConfig
import com.tcc.doctorweather.features.main.domain.Weather
import com.tcc.doctorweather.features.main.domain.WeatherRepository
import com.tcc.doctorweather.features.main.ui.WeatherScreenAction.UpdateCurrentWeather
import com.tcc.doctorweather.features.main.ui.WeatherScreenAction.UpdateInProgress
import com.tcc.doctorweather.utils.RepoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    lateinit var state: StateFlow<WeatherScreenUiState>
    private val _weatherScreenUiState =
        MutableStateFlow(WeatherScreenUiState(currentWeather = Weather()))

    private val _snackbarMessages = MutableSharedFlow<String>()
    val snackbarMessages = _snackbarMessages.asSharedFlow()

    private var previouslySearchedCoordinates: Pair<String, String> =
        Pair(BuildConfig.DEFAULT_LAT, BuildConfig.DEFAULT_LON)

    init {
        viewModelScope.launch {
            state = _weatherScreenUiState.stateIn(viewModelScope)
        }
        executeGetCurrentWeather()
    }

    /**
     * Retrieves current weather at location and updates UI state
     * Defaults to values from BuildConfig
     */
    fun executeGetCurrentWeather(
        lat: String = previouslySearchedCoordinates.first,
        lon: String = previouslySearchedCoordinates.second
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(UpdateInProgress(true))

            val repoResult = repository.getCurrentWeather(
                lat = lat,
                lon = lon
            )

            previouslySearchedCoordinates = Pair(lat, lon)

            when (repoResult) {
                is RepoResult.Error -> {
                    _snackbarMessages.emit("Error fetching weather data, please try again later.")
                }

                is RepoResult.Success -> {
                    repoResult.data?.let { updateUiState(UpdateCurrentWeather(it)) }
                }
            }

            updateUiState(UpdateInProgress(false))
        }
    }

    /**
     * Formats [unixTimestamp] to human readable dd.MM.yyyy HH:mm
     */
    fun formatUnixTimestamp(unixTimestamp: Long): String {
        val date = Date(unixTimestamp)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(date)
    }

    /**
     * Updates the ui state of [WeatherScreen].
     */
    private fun updateUiState(action: WeatherScreenAction) {
        _weatherScreenUiState.update { latest ->
            action.updateState(latest)
        }
    }
}