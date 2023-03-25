package com.onc.ddm_proyectofinal_onc.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.databinding.FragmentDiaryBinding
import com.onc.ddm_proyectofinal_onc.model.WeatherForecastItem
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.utils.Constants
import com.onc.ddm_proyectofinal_onc.utils.Constants.getSerializableCompat
import com.onc.ddm_proyectofinal_onc.view.activities.MainActivity
import com.onc.ddm_proyectofinal_onc.view.adapters.LocalidadesRecyViewAdapater
import com.onc.ddm_proyectofinal_onc.view.adapters.WeatherForecastAdapter
import com.onc.ddm_proyectofinal_onc.viewmodel.LocalidadSelectedViewModel
import com.onc.ddm_proyectofinal_onc.viewmodel.WeatherForecastViewModel


class DiaryFragment : Fragment() {

    private  var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private var localidad: Localidad? = null

    private val weatherForecastViewModel: WeatherForecastViewModel by viewModels()

    //Adaptador del RecycleView
    private lateinit var adapter : WeatherForecastAdapter

    companion object {
        fun newInstance(localidad: Localidad) =
            DiaryFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Constants.KEY_LOCALIDAD, localidad)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
         //   localidad = it.getSerializableCompat(Constants.KEY_LOCALIDAD, Localidad::class.java)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        val view  = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Establece el observador cuando se cambia la localidad seleccionada

      //Se registra la actividad para observar a la localidad seleccionada
        val localidadObserver = androidx.lifecycle.Observer<Localidad>{
            localidad = it
            fiilCArdHeader()
            weatherForecastViewModel.setLocalidad(it)
        }

        val activityParent = this.activity as MainActivity
        activityParent.localidadSelectedViewmodel.localidadLiveData.observe(this.requireActivity(), localidadObserver)



        //Establece el observador para el cambio en el repositorio
        weatherForecastViewModel.weatherForecastLiveData.observe(viewLifecycleOwner, Observer { pronostico ->
            if (pronostico != null && pronostico.list != null){
                val items = ArrayList<WeatherForecastItem>(pronostico.list)
                adapter.updateItems(items)

                val sunRise = Constants.dateTimeToString(pronostico.city?.sunrise?.toLong()?:0, "hh:mm")
                val sunSet = Constants.dateTimeToString(pronostico.city?.sunset?.toLong()?:0, "hh:mm")
                binding.textViewSunRiseSet.text = "${sunRise} | ${sunSet}"
            }
            else { adapter.updateItems(ArrayList<WeatherForecastItem>()) }

        })

        if (localidad == null) {
            localidad = Localidad(0, "Ciudad Universitaria",
                "C.U., Ciudad de México", "Ciudad de México", "México",
                19.31888949, -99.1843676, "")
        }
        weatherForecastViewModel.setLocalidad(localidad!!)

        fiilCArdHeader()

        adapter = WeatherForecastAdapter(weatherForecastViewModel.getWeatherForecastList(), this.requireContext())
        binding.rvListaForecast.itemAnimator = DefaultItemAnimator()
        binding.rvListaForecast.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.rvListaForecast.adapter = adapter


    }

    fun fiilCArdHeader(){
        binding.textViewCiudadName.text = localidad?.name?:getString(R.string.menu_item_ciudad)
        binding.textViewDetalle.text = localidad?.description?:getString(R.string.diaryFragment_cardview_descripcion)
        binding.textViewLat.text = "${localidad?.latitude?:0.0}, "
        binding.textViewLon.text = "${localidad?.longitude?:0.0}, "

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}