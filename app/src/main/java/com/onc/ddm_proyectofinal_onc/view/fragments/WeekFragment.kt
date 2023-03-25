package com.onc.ddm_proyectofinal_onc.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.databinding.FragmentDiaryBinding
import com.onc.ddm_proyectofinal_onc.databinding.FragmentWeekBinding
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.utils.Constants
import com.onc.ddm_proyectofinal_onc.utils.Constants.getSerializableCompat


class WeekFragment : Fragment() {

    private  var _binding: FragmentWeekBinding? = null
    private val binding get() = _binding!!

    private var localidad: Localidad? = null

    companion object {
        fun newInstance(localidad: Localidad) =
            WeekFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Constants.KEY_LOCALIDAD, localidad)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            localidad = it.getSerializableCompat(Constants.KEY_LOCALIDAD, Localidad::class.java)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        val view  = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}