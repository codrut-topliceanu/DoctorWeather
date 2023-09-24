# DoctorWeather
A small demo app that enables users to check the weather at specific coordinates using openweathermap.org API.

_A minimum of Android 10 is required for this app._ 

It's build around an MVVM architecture using the following techstack:
 * Jetpack Compose
 * Jetpack Compose Navigation (but not in use at the moment)
 * Material3 UI components
 * Hilt for dependency injection
 * Retrofit2 for Api calls + Moshi for deserialization
 * Coil for async image loading


### How to run this project:
1. Download source code.
2. In the root folder of the project create a new file called `apikey.properties`.
3. Get an API key by signing up here: https://openweathermap.org/api/ ).
4. Add the following lines to `apikey.properrties`: 
`API_KEY = "your api key"`
`DEFAULT_LAT = "your preferred default latitude"`
`DEFAULT_LON = "your preferred default longitude"`
5. Open in Android Studio.
6. Build and Run.

### Possible improvements:
* Launch (Google) Maps intent to receive coordinates for a specific location.
* Better input sanitization of the current lat/long input fields.
* Testing.





