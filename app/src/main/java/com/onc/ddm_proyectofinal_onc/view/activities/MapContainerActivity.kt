package com.onc.ddm_proyectofinal_onc.view.activities

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.databinding.ActivityMapContainerBinding
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.utils.Constants
import com.onc.ddm_proyectofinal_onc.utils.Constants.getSerializableCompat
import com.onc.ddm_proyectofinal_onc.utils.ManejadorPermisos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapContainerActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    OnMapLongClickListener{

    private lateinit var binding: ActivityMapContainerBinding

    var localidad: Localidad? = null
    var marker: MarkerOptions ? = null

    //Para googleMaps
    private lateinit var map : GoogleMap
    private lateinit var geocoder: Geocoder

    //PAra manejar los permisos
    private  val manejadorPermisos = ManejadorPermisos(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let { bundle ->
            if (bundle.containsKey(Constants.KEY_LOCALIDAD)) {
                localidad = bundle.getSerializableCompat(Constants.KEY_LOCALIDAD, Localidad::class.java)
            }
        }

        binding.toolbarBackToMain.setOnClickListener {
            if (localidad != null) {
                var intentResult = Intent()
                intentResult.putExtra(Constants.KEY_LOCALIDAD, localidad)
                setResult(Activity.RESULT_OK, intentResult)
            }
            else {
                setResult(Activity.RESULT_CANCELED)
            }
            finish()
        }
        geocoder = Geocoder(this)

        searchViewConfig()
        manejadorPermisos.addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        manejadorPermisos.addPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapContainerFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    fun searchViewConfig(){

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                //Imprementar la búsqueda
                if (query != null && query.trim().isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val addressList = geocoder.getFromLocationName(query, 1)
                            val address = addressList?.firstOrNull()

                            if (address!=null) {
                                Log.d(Constants.LOGTAG, "Decoder result: ${address}")
                                //Address[addressLines=[0:"México"],feature=México,admin=null,sub-admin=null,locality=null,thoroughfare=null,postalCode=null,countryCode=MX,countryName=México,hasLatitude=true,latitude=23.634501,hasLongitude=true,longitude=-102.55278399999999,phone=null,url=null,extras=null]
                                //Address[addressLines=[0:"C.U., Ciudad de México, CDMX, México"],feature=Ciudad Universitaria,admin=Ciudad de México,sub-admin=null,locality=Ciudad de México,thoroughfare=null,postalCode=null,countryCode=MX,countryName=México,hasLatitude=true,latitude=19.318889499999997,hasLongitude=true,longitude=-99.1843676,phone=null,url=null,extras=null]
                                var id = 0
                                val nombre = address.featureName
                                val estado = address.locality
                                val pais = address.countryName
                                val descripcion = address.getAddressLine(0)
                                val lat = address.latitude
                                val lon = address.longitude

                                if (localidad != null) {
                                    id = localidad?.id?: 0
                                }
                                localidad = Localidad(
                                        id,
                                        nombre,
                                        descripcion,
                                        estado,
                                        pais,
                                        lat,
                                        lon,
                                        ""
                                    )

                                withContext(Dispatchers.Main) {
                                    updateMarker()
                                }

                            }
                            else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@MapContainerActivity,
                                        getString(R.string.map_container_search_not_found),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }
                        catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Log.d(Constants.LOGTAG,  getString(R.string.map_container_search_error) + ": " + e.message)
                                Toast.makeText(
                                    this@MapContainerActivity,
                                    getString(R.string.map_container_search_error),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                    }

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }


    private fun updateOrRequestPermissions() {
        if (!manejadorPermisos.hasAllPermission()){
            manejadorPermisos.requestPermission()
        } else {

            //Tenemos los permisos
            //  map.isMyLocationEnabled = true

            /*


            //Actualiza la localización de mi ubicación
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000, //Cada 2 segundos actualiza la ubicación
                10F,
                this
            )
             */
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ManejadorPermisos.PERMISO_LOCALIZACION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Se obtuvo el permiso
                    updateOrRequestPermissions()
                } else {
                    if (shouldShowRequestPermissionRationale(permissions[0])) {
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.map_container_activity_aler_title))
                            .setMessage(getString(R.string.map_container_activity_aler_message))
                            .setPositiveButton(
                                getString(R.string.map_container_activity_aler_positive_button),
                                DialogInterface.OnClickListener { _, _ ->
                                    updateOrRequestPermissions()
                                })
                            .setNegativeButton(
                                getString(R.string.map_container_activity_alernegative_button),
                                DialogInterface.OnClickListener { dialog, _ ->
                                    dialog.dismiss()
                                    finish()
                                })
                            .create()
                            .show()
                    } else {
                        //Si el usuario no quiere que nunca se le vuelva a preguntar por el permiso
                        Toast.makeText(
                            this,
                            getString(R.string.map_container_activity_toast_denegate_message),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMapa: GoogleMap) {
        map = googleMapa
        map.setPadding(0, 50, 0, 0)
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapLongClickListener(this)
        map.setOnMarkerDragListener(this)
        updateMarker()
        manejadorPermisos.requestPermission()
    }




    fun updateMarker() {
        if (localidad != null && localidad?.latitude != null && localidad?.longitude != null) {
            val coordinates = LatLng(localidad?.latitude!!, localidad?.longitude!!)
            val hayMarcador =  (marker != null)
            marker = MarkerOptions()
                .position(coordinates)
                .title(localidad?.name)
                .snippet("${localidad?.state}, ${localidad?.country}")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_pin_icon))

            if (hayMarcador) {
                map.clear()
            }

                map.addMarker(marker!!)
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(coordinates, 14f),
                    4000,
                    null
                )

        }
    }


    override fun onRestart() {
        super.onRestart()
        if (!::map.isInitialized) return

        manejadorPermisos.requestPermission()
    }

    override fun onMarkerDrag(marcador: Marker) {
        // Log.d("LOGS", "onMarkerDragStart: ${marcador.position}")
    }

    override fun onMarkerDragEnd(marcador: Marker) {
        Log.d("LOGS", "onMarkerDragEnd: ${marcador.position}")
        if (localidad == null ) {
            localidad = Localidad(0, "Nueva ubicacion", "", "", "",
                marcador.position.latitude, marcador.position.longitude, "")
        }
        else {
            localidad?.latitude = marcador.position.latitude
            localidad?.longitude = marcador.position.longitude
        }
        updateMarker()

    }

    override fun onMarkerDragStart(marcador: Marker) {
       // Log.d("LOGS", "onMarkerDragStart: ${marcador.position}")
    }

    override fun onMapLongClick(coord: LatLng) {
        if (localidad == null ) {
            localidad = Localidad(0, "Nueva ubicacion", "", "", "",
                coord.latitude, coord.longitude, "")
        }
        else {
            localidad?.latitude =coord.latitude
            localidad?.longitude = coord.longitude
        }
        updateMarker()
    }


}


