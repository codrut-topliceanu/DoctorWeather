package com.tcc.doctorweather.features.main.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tcc.doctorweather.R
import com.tcc.doctorweather.ui.compose.DrWeatherSnackBar
import com.tcc.doctorweather.ui.compose.LocationAlertDialog
import com.tcc.doctorweather.ui.compose.unboundedRippleClickable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {

    val viewState by weatherViewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewState.inProgress,
        onRefresh = weatherViewModel::executeGetCurrentWeather
    )
    val openLocationDialog = remember { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {
        weatherViewModel.snackbarMessages.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    if (openLocationDialog.value) {
        LocationAlertDialog(setShowDialog = {
            openLocationDialog.value = it
        }) { latitude, longitude ->
            weatherViewModel.executeGetCurrentWeather(lat = latitude, lon = longitude)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                actions = {
                    Image(
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .padding(end = 10.dp)
                            .unboundedRippleClickable {
                                openLocationDialog.value = true
                            },
                        painter = painterResource(
                            id = R.drawable.baseline_search_24
                        ),
                        contentScale = ContentScale.Inside,
                        contentDescription = "Search icon button",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer

                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                DrWeatherSnackBar(data)
            }
        }
    ) { paddingValues ->

        if (viewState.inProgress) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(start = 10.dp, end = 10.dp),
            horizontalAlignment = CenterHorizontally
        ) {

            PullRefreshIndicator(
                refreshing = viewState.inProgress,
                state = pullRefreshState,
                modifier = Modifier.align(CenterHorizontally),
                backgroundColor = if (viewState.inProgress) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.secondary
                },
            )

            // Timezone + location + timestamp
            FillInTimeLocationTimestamp(viewState, weatherViewModel)

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )

            // Current weather icon
            val iconURL = viewState.currentWeather.currentWeatherIcon?.let {
                "https://openweathermap.org/img/wn/" +
                        "${viewState.currentWeather.currentWeatherIcon}@2x.png"
            } ?: run { "" }

            Log.i("Coil", "Attempting to load URL = $iconURL")

            Surface(
                modifier = Modifier
                    .height(100.dp)
                    .width(135.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(20)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .height(100.dp)
                        .width(135.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(iconURL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Weather Icon",
                    placeholder = painterResource(R.drawable.fallback_icon),
                    error = painterResource(R.drawable.fallback_icon),
                    fallback = painterResource(R.drawable.fallback_icon)
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )

            // Weather info/desc
            Text(
                text = "Current weather: ${viewState.currentWeather.currentWeather}",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = viewState.currentWeather.currentWeatherDescription,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            FillInSecondaryWeatherData(viewState)
        }

    }

}

@Composable
private fun FillInSecondaryWeatherData(viewState: WeatherScreenUiState) {
    // Temperature
    Text(
        text = "Temperature: ${viewState.currentWeather.temperature ?: "--"} " +
                viewState.currentWeather.temperatureUnits,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

    Text(
        text = "Feels like: ${viewState.currentWeather.tempFeelsLike ?: "--"} " +
                viewState.currentWeather.temperatureUnits,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )

    // Pressure
    Text(
        text = "Pressure: ${viewState.currentWeather.atmPressure ?: "--"} " +
                "hPa",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )

    // Humidity
    Text(
        text = "Humidity: ${viewState.currentWeather.humidity ?: "--"} " +
                "%",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )

    // Cloud percentage
    Text(
        text = "Cloud coverage: ${viewState.currentWeather.cloudsPercentage ?: "--"} " +
                "%",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )

    // UV index
    Text(
        text = "UV index: ${viewState.currentWeather.uvIndex ?: "--"} " +
                "%",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )

    // Wind speed
    Text(
        text = "Wind speed: ${viewState.currentWeather.windSpeed ?: "--"} " +
                viewState.currentWeather.windSpeedUnits,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun FillInTimeLocationTimestamp(
    viewState: WeatherScreenUiState,
    weatherViewModel: WeatherViewModel
) {
    Text(
        text = "Weather at ${viewState.currentWeather.timezone}",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

    Text(
        text = "Lat/Lon: ${viewState.currentWeather.lat} , ${viewState.currentWeather.lon}",
        style = MaterialTheme.typography.bodySmall,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )

    val date = viewState.currentWeather.timestamp?.let {
        weatherViewModel.formatUnixTimestamp(it)
    } ?: run { "--" }

    Text(
        text = "Last updated: $date",
        style = MaterialTheme.typography.bodySmall,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
fun WeatherScreenPreview() {
    WeatherScreen()
}

