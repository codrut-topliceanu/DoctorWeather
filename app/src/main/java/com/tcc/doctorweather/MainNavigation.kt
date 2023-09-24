package com.tcc.doctorweather

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.tcc.doctorweather.features.main.ui.WeatherScreen
import com.tcc.doctorweather.ui.theme.DoctorWeatherTheme

sealed class AppFeatures {
    sealed class Weather(val route: String) {
        object LocationWeather : Weather("location_weather")
        /* Extra screens of this feature would sit here as objects */
    }
    /* Extra app features would sit here as sealed classes */
}

/* This would normally be used to navigate between features/screens */
/**
 * Main compose navigation, handles all navigation inside the app.
 */
@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController()
) {
    DoctorWeatherTheme {
        /* This is the main navigation tree of the app. */
        NavHost(
            navController = navController,
            startDestination = AppFeatures.Weather::javaClass.name
        ) {
            /* This is a sub-tree of the app that handles navigation only within the Weather Feature */
            navigation(
                startDestination = AppFeatures.Weather.LocationWeather.route,
                route = AppFeatures.Weather::javaClass.name
            ) {

                composable(AppFeatures.Weather.LocationWeather.route) {
                    WeatherScreen()
                }

                /* This is where a different screen of the same feature would be created */
//                composable(AppFeatures.Weather.<....>route) {
//                    <.....>Screen()
//                }
            }
        }
    }
}

/* This method can be used to create and share a viewModel between the screens of the same navigation graph.
* For example the Weather screen may have another DetailedWeather screen inside the same feature, in such a scenario we could
* use the same viewModel to keep data alive, while still using the navigator. */

///**
// * Returns a shared instance of a ViewModel that can be accessed by multiple composable functions
// * within the same navigation graph. This function retrieves the ViewModel instance scoped to the
// * parent destination of the current destination, or the root destination if the current destination
// * has no parent. If there is no parent destination, this function returns a new instance of the
// * ViewModel.
// *
// * @param navController The NavController used for navigation.
// *
// * @returns A shared instance of the ViewModel.
// */
//@Composable
//inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
//    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
//    val parentEntry = remember(this) {
//        navController.getBackStackEntry(navGraphRoute)
//    }
//    return hiltViewModel(parentEntry)
//}