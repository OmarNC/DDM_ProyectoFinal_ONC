package com.onc.ddm_proyectofinal_onc.model.entities
import java.io.Serializable



data class Localidad(
    val id: Int = 0,
    var name: String?,
    var description: String?,
    var state: String?,
    var country: String?,
    var latitude: Double?,
    var longitude: Double?,
    var image_src: String?,
) : Serializable

object LocalidadFieldsName{
        const val ID = "LOCALIDAD_ID"
        const val NAME = "LOCALIDAD_NAME"
        const val DESCRIPTION = "LOCALIDAD_DESCRIPTION"
        const val STATE = "LOCALIDAD_STATE"
        const val COUNTRY = "LOCALIDAD_COUNTRY"
        const val LATITUDE = "LOCALIDAD_LATITUDE"
        const val LONGITUDE = "LOCALIDAD_LONGITUDE"
        const val IMAGE_SRC = "LOCALIDAD_IMAGE_SRC"
}
