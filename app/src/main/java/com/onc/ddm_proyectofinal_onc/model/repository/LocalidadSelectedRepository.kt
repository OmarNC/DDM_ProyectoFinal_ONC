package com.onc.ddm_proyectofinal_onc.model.repository

import android.content.Context
import android.content.SharedPreferences
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.model.entities.LocalidadFieldsName


class LocalidadSelectedRepository {
    companion object {
        val FILE_NAME = ".locadidadActual"
        private var _localidadSelected: Localidad? = null
        val localidadSelected get() = _localidadSelected!!


        fun newInstance(context: Context): LocalidadSelectedRepository {
           // loadData(context)
            return LocalidadSelectedRepository()
        }



        fun loadData(context: Context) {
            val sharedPreferences = context.applicationContext.getSharedPreferences(
                "${context.applicationContext.packageName}${FILE_NAME}",
                Context.MODE_PRIVATE
            ) ?: return
            with(sharedPreferences) {
                val id = getInt(LocalidadFieldsName.ID, 0)
                val name = getString(LocalidadFieldsName.NAME, "Ciudad Universitaria")
                val desc = getString(LocalidadFieldsName.DESCRIPTION, "C.U., Ciudad de México")
                val state = getString(LocalidadFieldsName.STATE, "Ciudad de México")
                val country = getString(LocalidadFieldsName.COUNTRY, "México")
                val lat = getDouble(LocalidadFieldsName.LATITUDE, 19.31888949)
                val lon = getDouble(LocalidadFieldsName.LONGITUDE, -99.1843676)
                val src = getString(LocalidadFieldsName.IMAGE_SRC, "")
                _localidadSelected= Localidad(id, name, desc, state, country, lat, lon, src)

            }
        }

        fun saveData(context: Context) {
            if (_localidadSelected == null) {
                return
            }
            val sharedPreferences = context.applicationContext
                .getSharedPreferences(
                    "${context.applicationContext.packageName}${FILE_NAME}",
                    Context.MODE_PRIVATE
                ) ?: return

            with(sharedPreferences.edit()) {
                putInt(LocalidadFieldsName.ID, localidadSelected!!.id)
                putString(LocalidadFieldsName.NAME, localidadSelected!!.name)
                putString(LocalidadFieldsName.DESCRIPTION, localidadSelected!!.description)
                putString(LocalidadFieldsName.STATE, localidadSelected!!.state)
                putString(LocalidadFieldsName.COUNTRY, localidadSelected!!.country)
                putDouble(LocalidadFieldsName.LATITUDE, localidadSelected!!.latitude!!)
                putDouble(LocalidadFieldsName.LONGITUDE, localidadSelected!!.longitude!!)
                putString(LocalidadFieldsName.IMAGE_SRC, localidadSelected!!.image_src)
                apply()
            }

        }


        inline fun SharedPreferences.Editor.putDouble(
            key: String,
            value: Double
        ): SharedPreferences.Editor {
            putLong(key, value.toRawBits())
            return this
        }

        inline fun SharedPreferences.getDouble(key: String, defValue: Double) =
            Double.fromBits(getLong(key, defValue.toRawBits()))

    }


    fun loadLocalidadSelected(context: Context): Localidad{
        LocalidadSelectedRepository.loadData(context)
        return localidadSelected
    }
    fun saveLocalidadSelected(context: Context, localidad: Localidad){

        LocalidadSelectedRepository._localidadSelected = localidad
        LocalidadSelectedRepository.saveData(context)

    }
}