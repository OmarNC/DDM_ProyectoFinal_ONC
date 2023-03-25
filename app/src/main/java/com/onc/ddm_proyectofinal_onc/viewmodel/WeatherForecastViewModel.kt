package com.onc.ddm_proyectofinal_onc.viewmodel

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.model.WeatherForecast
import com.onc.ddm_proyectofinal_onc.model.WeatherForecastItem
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.model.repository.LocalidadesRepository
import com.onc.ddm_proyectofinal_onc.model.repository.WeatherForecastRepository
import com.onc.ddm_proyectofinal_onc.network.IMyWeatherAPI
import com.onc.ddm_proyectofinal_onc.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherForecastViewModel(private val app : Application) : AndroidViewModel(app) {

    private var weatherForecast: WeatherForecast? = null
    private val _weatherForecastLiveData = MutableLiveData<WeatherForecast>()
    val weatherForecastLiveData: LiveData<WeatherForecast>  = _weatherForecastLiveData

    private var localidad: Localidad? = null
    private var _localidadLiveData = MutableLiveData<Localidad>()
    val localidadLiveData: LiveData<Localidad> = _localidadLiveData


    private fun loadData(localidad: Localidad) {
        CoroutineScope(Dispatchers.IO).launch {

            val call = Constants.getRetrofitWeater().create(IMyWeatherAPI::class.java)
                .getForecastWeather(
                    "forecast", localidad?.latitude, localidad?.longitude,
                    app.getString(R.string.openweather_apikey), "sp", "metric"
                )

            call.enqueue(object : Callback<WeatherForecast> {

                override fun onResponse(
                    call: Call<WeatherForecast>,
                    response: Response<WeatherForecast>
                ) {
                    Log.d(Constants.LOGTAG, "La respuesta del servicio: ${response.toString()}")
                    Log.d(Constants.LOGTAG, "Los datos recibidos: ${response.body()}")

                    weatherForecast = response.body()
                    if (weatherForecast != null) {
                        _weatherForecastLiveData.postValue(weatherForecast!!)
                    }

                }

                override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
                    Log.d(
                        Constants.LOGTAG,
                        "No se pudo establecer conexión al sitio: ${Constants.BASE_URL} \n " +
                                "ERROR: ${t.message}"
                    )

                    Toast.makeText(
                        this@WeatherForecastViewModel.getApplication(),
                        "${
                            Resources.getSystem()
                                .getString(R.string.today_fragment_fail_conection)
                        } ${Constants.BASE_URL}",
                        Toast.LENGTH_LONG
                    ).show()

                }
            })
        }
    }


    fun loadWeatherForecast(localidad: Localidad){
        this.localidad = localidad
        this._localidadLiveData.value = localidad
        loadData(localidad)
    }


    fun getWeatherForecastList():ArrayList<WeatherForecastItem> {
        if (weatherForecast!= null && weatherForecast?.list != null){
            return  ArrayList<WeatherForecastItem>(weatherForecast?.list!!)
        }
        return ArrayList<WeatherForecastItem>()
    }


    fun setLocalidad(localidad: Localidad){
        this.localidad = localidad
        this._localidadLiveData.value = localidad
        loadData(localidad)
    }

}