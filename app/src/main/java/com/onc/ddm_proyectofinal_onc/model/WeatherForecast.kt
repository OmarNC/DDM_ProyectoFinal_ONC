package com.onc.ddm_proyectofinal_onc.model


data class WeatherForecast(
    var city: City?,
    var cnt: Int?,
    var cod: String?,
    var list: List<WeatherForecastItem>?,
    var message: Int?
) {
    data class City(
        var coord: Coord?,
        var country: String?,
        var id: Int?,
        var name: String?,
        var population: Int?,
        var sunrise: Int?,
        var sunset: Int?,
        var timezone: Int?
    ) {
        data class Coord(
            var lat: Double?,
            var lon: Double?
        )
    }
}