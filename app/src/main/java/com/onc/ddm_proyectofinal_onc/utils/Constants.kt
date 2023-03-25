package com.onc.ddm_proyectofinal_onc.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    //const val URL_WEATHER = "https://" + BASE_URL
    //const val URL_POLLUTION = "http://" + BASE_URL

    //"https://api.openweathermap.org/data/2.5/weather?lang=se&units=metric&lat={lat}&lon={lon}&appid={APIkey}"
    //"https://api.openweathermap.org/data/2.5/forecast?lang=se&units=metric&lat={lat}&lon={lon}&appid={APIkey}"
    //"http://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={API key}"
    //"http://api.openweathermap.org/data/2.5/air_pollution/forecast?lat={lat}&lon={lon}&appid={API key}"
    //"http://api.openweathermap.org/data/2.5/air_pollution?lat=19.31888949&lon=-99.1843676&appid=496a0105344a523fa20133a783419a90


    const val LOGTAG = "LOGS"
    const val KEY_LOCALIDAD = "KEY_LOCALIDAD"



    fun getRetrofitWeater(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofitPollution(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun <T : Serializable?> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializable(
            key,
            clazz
        )!! else (getSerializable(key) as T)
    }





    fun <T : Serializable?> getSerializableCompat(intent: Intent?, name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent?.getSerializableExtra(name, clazz)!!
        else
            intent?.getSerializableExtra(name) as T
    }

    fun dateTimeToString(miliseconds: Long, format: String) : String
    {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = miliseconds*1000L
        //val simpleDateFormat  = SimpleDateFormat("dd/mm/yyyy h:mm a")
        val simpleDateFormat  = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(calendario.time)
    }

    fun dateTimeToString(date: Date, format: String) : String
    {
        val simpleDateFormat  = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(date)
    }


}


