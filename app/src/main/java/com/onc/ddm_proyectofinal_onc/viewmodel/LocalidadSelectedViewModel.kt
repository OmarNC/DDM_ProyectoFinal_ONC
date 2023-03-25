package com.onc.ddm_proyectofinal_onc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.model.repository.LocalidadSelectedRepository

class LocalidadSelectedViewModel(private val app : Application) : AndroidViewModel(app) {

   // private val context = getApplication<Application>().applicationContext

    private val localidadSelectedRepo = LocalidadSelectedRepository.newInstance(app.applicationContext)
    private var _localidadLiveData = MutableLiveData<Localidad>()
    val localidadLiveData: LiveData<Localidad> = _localidadLiveData

    init {
        loadLocalidadSelected()
    }

    fun setLocalidadSelected(localidad: Localidad){
        _localidadLiveData.postValue( localidad)
    }


    fun loadLocalidadSelected(){
        setLocalidadSelected(localidadSelectedRepo.loadLocalidadSelected(app.applicationContext))
    }
    fun saveLocalidadSelected(){
        localidadSelectedRepo.saveLocalidadSelected(app.applicationContext, _localidadLiveData.value!!)
    }

}