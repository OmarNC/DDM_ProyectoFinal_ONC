package com.onc.ddm_proyectofinal_onc.model

import com.google.gson.annotations.SerializedName

data class WeatherPollutionToday(
    var coord: Coord?,
    var list: List<Pollution?>?
) {
    data class Coord(
        var lat: Double?,
        var lon: Double?
    )
    data class Pollution(
        var components: ComponentsTmp?,
        var dt: Int?,
        @SerializedName("main")
        var main_tmp: MainTmp?
    ) {
        data class ComponentsTmp(
            var co: Double?,
            var nh3: Double?,
            var no: Double?,
            var no2: Double?,
            var o3: Double?,
            var pm10: Double?,
            var pm2_5: Double?,
            var so2: Double?
        )

        data class MainTmp(
            var aqi: Int?
        )
    }
}