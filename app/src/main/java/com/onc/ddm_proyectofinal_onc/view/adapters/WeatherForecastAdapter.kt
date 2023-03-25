package com.onc.ddm_proyectofinal_onc.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onc.ddm_proyectofinal_onc.databinding.RecycleviewItemLocalidadBinding
import com.onc.ddm_proyectofinal_onc.databinding.RecycleviewItemWeatherForecastBinding
import com.onc.ddm_proyectofinal_onc.model.WeatherForecast
import com.onc.ddm_proyectofinal_onc.model.WeatherForecastItem
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.utils.Constants
import com.onc.ddm_proyectofinal_onc.view.activities.IRecyViewItemPressedListener
import java.util.*
import kotlin.collections.ArrayList

class WeatherForecastAdapter (private val items: ArrayList<WeatherForecastItem>, private val contex: Context): RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastViewHolder>() {


    class WeatherForecastViewHolder(val binding: RecycleviewItemWeatherForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pronostico: WeatherForecastItem, context: Context) {

            binding.fechaHora.text = Constants.dateTimeToString(pronostico.dt?.toLong()!!, "hh:mm a dd/MM/yyyy")
            val weather = pronostico.weather?.firstOrNull()
            val icon = weather?.icon
            if (icon!=null){
                val strName = "ic_${icon!!}"
                val resID: Int =
                    context.resources.getIdentifier(strName, "drawable", context.packageName)
                if (resID != 0){ binding.ivWeatherIcon.setImageResource(resID) }
            }
            binding.textViewWeatherMain.text = weather?.main
            binding.textViewWeatherDescrip.text = weather?.description

            val pop = pronostico.pop ?: 0.0
            //val milim = pronostico.rain?.tres_h
            binding.textViewPronosticoLluvia.text = "${pop*100} %"
            //binding.textViewNubocidad.text = "${milim} mm"

            binding.texViewTemp.text = "${pronostico.main?.temp} °C"

            val max = pronostico.main?.temp_max
            val min = pronostico.main?.temp_min
            binding.textViewTempMaxMin.text = "${min}°C | ${max} °C"

            binding.texViewHumedad.text="${pronostico.main?.humidity} %"
            binding.texViewPresion.text="${pronostico.main?.pressure} hPa"

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        val binding = RecycleviewItemWeatherForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {

      //  Log.d(Constants.LOGTAG, "Bind items[${position}] = ${items[position].dt_txt} : ${items.size}")
        holder.bind(items[position], contex)
    }


    fun updateItems(newItems: ArrayList<WeatherForecastItem>)
    {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
        Log.d(Constants.LOGTAG, "Se ha actualizado la lista Size: ${items.size}" )

    }

}