package com.onc.ddm_proyectofinal_onc.model.repository.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad

import kotlin.collections.ArrayList

class LocalidadesSqlHelper (context : Context): SQLiteOpenHelper(context, DATEBASE_NAME, null, DATEBASE_VERSION) {

    companion object {
        private const val DATEBASE_VERSION = 1
        private const val DATEBASE_NAME = "localidades.db"

        //Nombre de la tabla
        private const val TBL_LOCALIDADES = "tbl_localidades"


        //Nombre de los campos de la tabla
        private const val ID = "id"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val STATE = "state"
        private const val COUNTRY = "country"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val IMAGE_SRC = "image_src"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val strSqlCreate = "CREATE TABLE $TBL_LOCALIDADES ("+
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME TEXT, " +
                "$DESCRIPTION TEXT, " +
                "$STATE TEXT, " +
                "$COUNTRY TEXT, " +
                "$LATITUDE REAL, " +
                "$LONGITUDE TEXT, " +
                "$IMAGE_SRC TEXT)"
        db?.execSQL(strSqlCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val strSqlUpdate = "DROP TABLE IF EXISTS $TBL_LOCALIDADES"
        db?.execSQL(strSqlUpdate)
        onCreate(db)
    }

    fun deleteDB() {
        val strSqlUpdate = "DROP TABLE IF EXISTS $TBL_LOCALIDADES"
        val db = writableDatabase
        db?.execSQL(strSqlUpdate)
    }

    fun insert(localidad: Localidad): Long
    {
        val db = writableDatabase
        val contentValue = ContentValues().apply{
            put(NAME, localidad.name)
            put(DESCRIPTION, localidad.description)
            put(STATE, localidad.state)
            put(COUNTRY, localidad.country)
            put(LATITUDE, localidad.latitude)
            put(LONGITUDE, localidad.longitude)
            put(IMAGE_SRC, localidad.image_src)
        }
        val result = db.insert(TBL_LOCALIDADES, null, contentValue)
        db.close()
        return result
    }


    fun getAllLocalidades() : ArrayList<Localidad>{
        val localidadesList = arrayListOf<Localidad>()
        var strQuery = "SELECT * FROM $TBL_LOCALIDADES"
        val db = readableDatabase

        val cursor : Cursor?

        try {
            cursor = db.rawQuery(strQuery, null)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            return  localidadesList
        }


        var id: Int = 0
        var name : String = ""
        var description : String = ""
        var state : String = ""
        var country : String = ""
        var lat: Double = 0.0
        var lon: Double  = 0.0
        var srcImage : String = ""


        with(cursor) {
            while(cursor.moveToNext())
            {
                id = getInt(cursor.getColumnIndexOrThrow(ID))
                name = getString(cursor.getColumnIndexOrThrow(NAME))
                description = getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                state  = getString(cursor.getColumnIndexOrThrow(STATE))
                country = getString(cursor.getColumnIndexOrThrow(COUNTRY))
                lat = getDouble(cursor.getColumnIndexOrThrow(LATITUDE))
                lon = getDouble(cursor.getColumnIndexOrThrow(LONGITUDE))
                srcImage = getString(cursor.getColumnIndexOrThrow(IMAGE_SRC))

                val localidad = Localidad(id, name, description, state, country, lat, lon, srcImage)
                localidadesList.add(localidad)
            }
        }
        cursor.close()

        return  localidadesList
    }


    fun updateLocalidad(localidad: Localidad) : Int
    {
        var db = writableDatabase
        var conentValue = ContentValues().apply {
            put(NAME, localidad.name)
            put(DESCRIPTION, localidad.description)
            put(STATE, localidad.state)
            put(COUNTRY, localidad.country)
            put(LATITUDE, localidad.latitude)
            put(LONGITUDE, localidad.longitude)
            put(IMAGE_SRC, localidad.image_src)
        }

        val result = db.update(TBL_LOCALIDADES, conentValue, "id=?" , arrayOf("${localidad.id}"))
        db.close()
        return result
    }

    fun deleteLocalidad(localidadId: Int) : Int {
        val db = writableDatabase
        val result = db.delete(TBL_LOCALIDADES, "id=?", arrayOf("$localidadId"))
        db.close()
        return result
    }

}