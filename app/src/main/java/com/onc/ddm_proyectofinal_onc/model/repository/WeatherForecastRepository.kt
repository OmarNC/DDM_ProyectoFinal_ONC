package com.onc.ddm_proyectofinal_onc.model.repository

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.model.WeatherForecast
import com.onc.ddm_proyectofinal_onc.model.WeatherForecastItem
import com.onc.ddm_proyectofinal_onc.model.WeatherNow
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.model.repository.db.LocalidadesSqlHelper
import com.onc.ddm_proyectofinal_onc.network.IMyWeatherAPI
import com.onc.ddm_proyectofinal_onc.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherForecastRepository {
    companion object {
        var weatherForecast: WeatherForecast? = null

        fun newInstance(localidad: Localidad): WeatherForecastRepository {
            loadData(localidad)
            return WeatherForecastRepository()
        }

        fun loadData(localidad: Localidad) {
            CoroutineScope(Dispatchers.IO).launch {

                val call = Constants.getRetrofitWeater().create(IMyWeatherAPI::class.java)
                    .getForecastWeather(
                        "forecast", localidad?.latitude, localidad?.longitude,
                        Resources.getSystem().getString(R.string.openweather_apikey), "sp", "metric"
                    )

                call.enqueue(object : Callback<WeatherForecast> {

                    override fun onResponse(
                        call: Call<WeatherForecast>,
                        response: Response<WeatherForecast>
                    ) {
                        Log.d(Constants.LOGTAG, "La respuesta del servicio: ${response.toString()}")
                        Log.d(Constants.LOGTAG, "Los datos recibidos: ${response.body()}")

                        weatherForecast = response.body()

                    }

                    override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
                        Log.d(
                            Constants.LOGTAG,
                            "No se pudo establecer conexión al sitio: ${Constants.BASE_URL} \n " +
                                    "ERROR: ${t.message}"
                        )
                        /*
                        Toast.makeText(
                            context,
                            "${
                                Resources.getSystem()
                                    .getString(R.string.today_fragment_fail_conection)
                            } ${Constants.BASE_URL}",
                            Toast.LENGTH_LONG
                        ).show()

                         */
                    }
                })
            }
        }
    }


    fun loadForecastWeather(localidad: Localidad) {
        loadData(localidad)
    }

   fun getForecastWeatherItemList(): ArrayList<WeatherForecastItem>? {
        if (weatherForecast != null && weatherForecast?.list != null) {
            return ArrayList<WeatherForecastItem>(weatherForecast?.list)
        }
       return null
    }
}
