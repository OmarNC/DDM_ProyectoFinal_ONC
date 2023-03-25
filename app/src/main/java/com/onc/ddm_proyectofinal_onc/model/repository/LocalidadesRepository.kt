package com.onc.ddm_proyectofinal_onc.model.repository

import android.content.Context
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.model.repository.db.LocalidadesSqlHelper


class LocalidadesRepository {
    companion object {
        lateinit var localidadDB: LocalidadesSqlHelper

        fun newInstance(context: Context): LocalidadesRepository {
            localidadDB = LocalidadesSqlHelper(context)
            return LocalidadesRepository()
        }
    }

    fun insert(localidad: Localidad): Long {
        return localidadDB.insert(localidad)
    }

    fun getAllLocalidades(): ArrayList<Localidad> {
        return localidadDB.getAllLocalidades()
    }

    fun updateLocalidad(localidad: Localidad): Int {
        return localidadDB.updateLocalidad(localidad)
    }

    fun deleteLocalidad(localidadId: Int): Int {
        return localidadDB.deleteLocalidad(localidadId)
    }


     fun deleteDB() {
         localidadDB.deleteDB()
    }

}