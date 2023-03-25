package com.onc.ddm_proyectofinal_onc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.model.repository.LocalidadesRepository

class LocalidadesListViewModel (app : Application) : AndroidViewModel(app) {

    private val localidadesRepository = LocalidadesRepository.newInstance(app.applicationContext)
    val localidadesList = MutableLiveData<ArrayList<Localidad>>()

    fun getLocalidadesList() {
        val localidades = localidadesRepository.getAllLocalidades()
        localidadesList.postValue(localidades)
    }

    fun insertItem (localidad : Localidad)
    {
        localidadesRepository.insert(localidad)
        getLocalidadesList()
    }

    fun updateItem(localidad: Localidad): Int {

        return localidadesRepository.updateLocalidad(localidad)
    }

    fun deleteItem(localidadId: Int): Int {
        return localidadesRepository.deleteLocalidad(localidadId)
    }

    fun deleteDb() {
        localidadesRepository.deleteDB()
    }

}