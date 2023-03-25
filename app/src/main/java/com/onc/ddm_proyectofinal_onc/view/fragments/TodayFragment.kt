package com.onc.ddm_proyectofinal_onc.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.databinding.FragmentTodayBinding
import com.onc.ddm_proyectofinal_onc.model.WeatherPollutionToday
import com.onc.ddm_proyectofinal_onc.model.WeatherNow
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.network.IMyWeatherAPI
import com.onc.ddm_proyectofinal_onc.utils.Constants
import com.onc.ddm_proyectofinal_onc.utils.Constants.getSerializableCompat
import com.onc.ddm_proyectofinal_onc.view.activities.MainActivity
import com.onc.ddm_proyectofinal_onc.viewmodel.LocalidadSelectedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

enum class GasName{SO2, NO2, PM10, PM2_5, O3, CO, NH3, NO}

class TodayFragment : Fragment() {

    private  var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    //Data
    private var localidad: Localidad? = null
    private var weatherNow: WeatherNow? = null
    private var weatherPollutionNow: WeatherPollutionToday? = null

    companion object {
        fun newInstance(localidad: Localidad) =
            TodayFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Constants.KEY_LOCALIDAD, localidad)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
          //  localidad = it.getSerializableCompat(Constants.KEY_LOCALIDAD, Localidad::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val view  = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Se registra la actividad para observar a la localidad seleccionada
        val localidadObserver = androidx.lifecycle.Observer<Localidad>{
            localidad = it
            loadData()
            loadDataPollution()
        }

        val activityParent = this.activity as MainActivity
        activityParent.localidadSelectedViewmodel.localidadLiveData.observe(this.requireActivity(), localidadObserver)

        //Detalle del pronóstico
        binding.chBxDetalle.isChecked = false
        binding.chBxDetalle.setOnCheckedChangeListener { boton, isChecked ->
            if (isChecked) {
                binding.cardGroup.visibility = View.VISIBLE
            } else {
                binding.cardGroup.visibility = View.GONE

            }
        }

        //Detalle de la contaminación
        binding.chBxDetalle2.isChecked = false
        binding.chBxDetalle2.setOnCheckedChangeListener { boton, isChecked ->
            if (isChecked) {
                binding.cardGroup2.visibility = View.VISIBLE
            } else {
                binding.cardGroup2.visibility = View.GONE

            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
        loadDataPollution()
    }

    private fun loadData()
    {
        CoroutineScope(Dispatchers.IO).launch {

            val call = Constants.getRetrofitWeater().create(IMyWeatherAPI::class.java)
                .getCurrentWeather("weather", localidad?.latitude, localidad?.longitude,
                    getString(R.string.openweather_apikey), "sp", "metric")

            call.enqueue(object : Callback<WeatherNow> {

                override fun onResponse(
                    call: Call<WeatherNow>,
                    response: Response<WeatherNow>
                ) {
                    Log.d(Constants.LOGTAG, "La respuesta del servicio: ${response.toString()}")
                    Log.d(Constants.LOGTAG, "Los datos recibidos: ${response.body()}")
                    weatherNow = response.body()

                    fillViewControls()

                }

                override fun onFailure(call: Call<WeatherNow>, t: Throwable) {
                    Log.d(
                        Constants.LOGTAG,
                        "No se pudo establecer conexión al sitio: ${Constants.BASE_URL} \n " +
                                "ERROR: ${t.message}"
                            )
                    Toast.makeText(
                        this@TodayFragment.requireContext(),
                        "${getString(R.string.today_fragment_fail_conection)} ${Constants.BASE_URL}",
                        Toast.LENGTH_LONG
                    ).show()
                    }
            })
        }
    }


    private fun fillViewControls()
    {

        if (weatherNow != null) {
            binding.textViewName.text = localidad?.name ?: "Ciudad"
            if (weatherNow?.dt != null){
                binding.textViewDate.text = Constants.dateTimeToString(weatherNow?.dt!!.toLong(), "hh:mm dd/MM/yyyy")
            }
            else{
                binding.textViewDate.text = Constants.dateTimeToString(Date(), "hh:mm dd/MM/yyyy")
            }

            binding.textViewTempActual.text = "%.1f".format(weatherNow?.main?.temp) + "°C"


            val strIcon = weatherNow?.weather?.firstOrNull()?.icon

            if (strIcon != null) {
                val idImage = getImageIconWeather(strIcon!!)
                if (idImage!=null) {
                    binding.imageViewPronostico.setImageResource(idImage!!)
                }
            }



            binding.textViewHumedad.text = "${weatherNow?.main?.humidity} %"
            binding.textViewVelViento.text = "${weatherNow?.wind?.speed} m/s"

            binding.textViewDetalleNow.text = "${weatherNow?.weather?.firstOrNull()?.description}"

            binding.textViewTempDetail.text = "${weatherNow?.main?.temp} °C | ${weatherNow?.main?.feels_like} °C"
            binding.textViewPresionDetail.text = "${weatherNow?.main?.pressure} hPa"

            if ( weatherNow?.sys != null) {
                binding.textViewSunRiseSet.text = Constants.dateTimeToString(weatherNow?.sys?.sunrise!!.toLong(), "hh:mm") +" | "+
                        Constants.dateTimeToString(weatherNow?.sys?.sunset!!.toLong(), "hh:mm")
            }
            else{
                binding.textViewDate.text =""
            }
            binding.textViewDirViento.text = "${weatherNow?.wind?.speed}m/s | ${weatherNow?.wind?.deg}°"

            /*
            binding.imageViewPronostico
            //var url ="http://openweathermap.org/img/wn/" + weatherNow?.weather?.get(0)?.icon + "@2x.png"
            var url ="ic_" + weatherNow?.weather?.get(0)?.icon + ".png"

            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.circulo_naranja)
                .into(binding.imageViewPronostico)
             */
        }

    }

    private fun loadDataPollution()
    {
        CoroutineScope(Dispatchers.IO).launch {

            val call = Constants.getRetrofitPollution().create(IMyWeatherAPI::class.java)
                .getCurrentPollution("air_pollution", localidad?.latitude, localidad?.longitude,
                    getString(R.string.openweather_apikey))

            call.enqueue(object : Callback<WeatherPollutionToday> {

                override fun onResponse(
                    call: Call<WeatherPollutionToday>,
                    response: Response<WeatherPollutionToday>
                ) {
                    Log.d(Constants.LOGTAG, "La respuesta del servicio: ${response.toString()}")
                    Log.d(Constants.LOGTAG, "Los datos recibidos: ${response.body()}")

                    weatherPollutionNow = response.body()

                    fillViewControlsPollution()

                }

                override fun onFailure(call: Call<WeatherPollutionToday>, t: Throwable) {
                    Log.d(
                        Constants.LOGTAG,
                        "No se pudo establecer conexión al sitio: ${Constants.BASE_URL} \n " +
                                "ERROR: ${t.message}"
                    )
                    Toast.makeText(
                        this@TodayFragment.requireContext(),
                        "${getString(R.string.today_fragment_fail_conection)} ${Constants.BASE_URL}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    private fun fillViewControlsPollution()
    {


        if (weatherPollutionNow != null) {
            val  pollution = weatherPollutionNow?.list?.firstOrNull()
            if (pollution == null) {
                return
            }

            binding.calidadAire.text = "${getString(R.string.diaryFragment_cardview_CalidadAire)}: ${pollution.main_tmp?.aqi}"
            binding.textViewDate2.text =  Constants.dateTimeToString(pollution?.dt!!.toLong(), "hh:mm dd/mm/yyyy")

            binding.textViewCalidaActual.text = getQualitativeName(pollution.main_tmp?.aqi)

            binding.imageViewIndCalidad.setImageResource(getImageIconSentiment(pollution.main_tmp?.aqi))

            var valor = pollution.components?.co
            binding.textViewMonoCarb.text = "${valor.toString()} ug/m3"
            binding.ivCO.setImageResource(
                getImageIconSentiment(
                    getQualitativeIndexByConcentration(valor?:0.0, GasName.CO)
                ))
            valor = pollution.components?.no
            binding.textViewMonoNitro.text = "${valor.toString()} ug/m3"

            /* No afectan al cálculo del IQA
            binding.ivNO.setImageResource(
                getImageIconSentiment(
                    getQualitativeIndexByConcentration(valor?:0.0, GasName.NO)
                ))

             */

            valor = pollution.components?.no2
            binding.textViewDioxNitro.text = "${valor.toString()} ug/m3"
            binding.ivNO2.setImageResource(
                getImageIconSentiment(
                    getQualitativeIndexByConcentration(valor?:0.0, GasName.NO2)
                ))

            valor = pollution.components?.o3
            binding.textViewOzono.text = "${valor.toString()} ug/m3"
            binding.ivO3.setImageResource(
                getImageIconSentiment(
                    getQualitativeIndexByConcentration(valor?:0.0, GasName.O3)
                ))

            valor = pollution.components?.so2
            binding.textViewDioxAzufre.text = "${valor.toString()} ug/m3"
            binding.ivSO2.setImageResource(
                getImageIconSentiment(
                    getQualitativeIndexByConcentration(valor?:0.0, GasName.SO2)
                ))

            valor = pollution.components?.nh3
            binding.textViewAmoniaco.text = "${valor.toString()} ug/m3"

            /* No afecta el índice IQA
            binding.ivNO2.setImageResource(
                getImageIconSentiment(
                    getQualitativeIndexByConcentration(valor?:0.0, GasName.NH3)
                ))

             */

            valor = pollution.components?.pm2_5
            binding.textViewPM25.text = "${valor.toString()} ug/m3"
            binding.ivPM25.setImageResource(
                getImageIconSentiment(
                    getQualitativeIndexByConcentration(valor?:0.0, GasName.PM2_5)
                ))

            valor = pollution.components?.pm10
            binding.textViewPM10.text = "${valor.toString()} ug/m3"
            binding.ivPM10.setImageResource(
                getImageIconSentiment(
                    getQualitativeIndexByConcentration(valor?:0.0, GasName.PM10)
                ))

            /*
            if (pollution.dt != null){
                binding.textViewDate.text = Constants.dateTimeToString(pollution.dt!!.toLong(), "hh:mm a dd/mm/yyyy")
            }
            else{
                binding.textViewDate.text = Constants.dateTimeToString(Date(), "hh:mm a dd/mm/yyyy")
            }

             */


            /*
            binding.imageViewPronostico
            //var url ="http://openweathermap.org/img/wn/" + weatherNow?.weather?.get(0)?.icon + "@2x.png"
            var url ="ic_" + weatherNow?.weather?.get(0)?.icon + ".png"

            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.circulo_naranja)
                .into(binding.imageViewPronostico)
             */
        }

    }

    private fun indefOfRange(valor: Double, array: Array<Double>):Int {
        var index = 0
        for((i, dato) in array.withIndex()){
            if (valor< dato){
                index = i
                break
            }
        }
        return  index
    }

    private fun getQualitativeIndexByConcentration(valor: Double, nombreGas: GasName) : Int{
        var index = 0
        var tablaSO2 = arrayOf(0.0, 20.0, 80.0, 250.0, 350.0)
        var tablaNO2 = arrayOf(0.0, 40.0, 70.0, 150.0, 200.0)
        var tablaPM10 = arrayOf(0.0, 20.0, 50.0, 100.0, 200.0)
        var tablaPM2_5 = arrayOf(0.0, 10.0, 25.0, 50.0, 75.0)
        var tablaO3 = arrayOf(0.0, 60.0, 100.0, 140.0, 180.0)
        var tablaC0 = arrayOf(0.0, 4400.0, 9400.0, 12400.0, 15400.0)
        when(nombreGas){
            GasName.SO2 -> {index = indefOfRange(valor, tablaSO2)}
            GasName.NO2 -> {index = indefOfRange(valor, tablaNO2)}
            GasName.PM10 -> {index = indefOfRange(valor, tablaPM10)}
            GasName.PM2_5 -> {index = indefOfRange(valor, tablaPM2_5)}
            GasName.O3 -> {index = indefOfRange(valor, tablaO3)}
            GasName.CO -> {index = indefOfRange(valor, tablaC0)}
            GasName.NH3 -> {}
            GasName.NO -> {}
            else -> {}
        }
        return index
    }

    private fun getQualitativeName(index: Int?) : String{
        if (index==null || index<0 ||index > 5) { return ""}
        var tabla = arrayOf("Sin determinar","Bueno", "Razonable", "Moderado", "Pobre", "Muy mala")
        return tabla.get(index)
    }

    private fun getImageIconSentiment(index: Int?): Int {
        var tabla = arrayOf(
            R.drawable.ic_sentiment_0,
            R.drawable.ic_sentiment_1,
            R.drawable.ic_sentiment_2,
            R.drawable.ic_sentiment_3,
            R.drawable.ic_sentiment_4,
            R.drawable.ic_sentiment_5
        )

        if (index == null || index < 0 || index > 5) {
            return tabla.get(0)
        } else {
            return tabla.get(index)
        }
    }

    private fun getImageIconWeather(nameImage: String): Int? {
        val strName = "ic_${nameImage}"
        val resID: Int = resources.getIdentifier(strName, "drawable", this.requireContext().packageName)
        return  resID
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}