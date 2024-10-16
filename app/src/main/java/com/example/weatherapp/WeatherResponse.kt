package com.example.weatherapp

data class WeatherResponse(
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>
)

data class Main(
    val temp: Float,
    val humidity: Int
)

data class Wind(
    val speed: Float
)

data class Weather(
    val description: String
)
