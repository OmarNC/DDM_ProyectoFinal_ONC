package com.onc.ddm_proyectofinal_onc.model

import com.google.gson.annotations.SerializedName

data class WeatherForecastItem(
    var clouds: Clouds?,
    var dt: Int?,
    var dt_txt: String?,
    var main: Main?,
    var pop: Double?,
    var rain: Rain?,
    var sys: Sys?,
    var visibility: Int?,
    var weather: List<Weather?>?,
    var wind: Wind?
) {
    data class Clouds(
        var all: Int?
    )

    data class Main(
        var feels_like: Double?,
        var grnd_level: Int?,
        var humidity: Int?,
        var pressure: Int?,
        var sea_level: Int?,
        var temp: Double?,
        var temp_kf: Double?,
        var temp_max: Double?,
        var temp_min: Double?
    )



    data class Rain(
        @SerializedName("3h")
        var tres_h: Double?
    )

    data class Sys(
        var pod: String?
    )

    data class Weather(
        var description: String?,
        var icon: String?,
        var id: Int?,
        var main: String?
    )

    data class Wind(
        var deg: Int?,
        var gust: Double?,
        var speed: Double?
    )
}
