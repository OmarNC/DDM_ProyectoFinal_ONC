package com.onc.ddm_proyectofinal_onc.view.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.databinding.ActivityAddLocationsBinding
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.utils.Constants
import com.onc.ddm_proyectofinal_onc.utils.Constants.getSerializableCompat
import com.onc.ddm_proyectofinal_onc.viewmodel.LocalidadesListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddLocationsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddLocationsBinding
    var localidad: Localidad? = null
    var modoEdicion:Boolean = false

    private val localidadViewModel : LocalidadesListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let { bundle ->
            if (bundle.containsKey(Constants.KEY_LOCALIDAD)) {
                localidad = bundle.getSerializableCompat(Constants.KEY_LOCALIDAD, Localidad::class.java)
                modoEdicion = true
                llenarCampos()
            }
        }

       //Buscar una localidad
       binding.extFab.setOnClickListener {

           val intent = Intent(this, MapContainerActivity::class.java)

           if (modoEdicion) {
               intent.putExtra(Constants.KEY_LOCALIDAD, localidad)
           }
           getLocationFromMap.launch(intent)

       }

        binding.addLicationFragmentBtnEliminar.setOnClickListener { botonEliminar() }
        binding.addLicationFragmentBtnEliminar2.setOnClickListener { botonEliminar() }

       //Regresa a la activity principal
       binding.toolbarBackToMain.setOnClickListener { finish() }

        //Guarda la localidad
        binding.addLicationFragmentBtnAgregar.setOnClickListener  { botonAgregar() }
        binding.addLicationFragmentBtnAgregar2.setOnClickListener  { botonAgregar() }

        //Oculta el teclado cuando se hace clic fuera de los edittext
        binding.linearLayout1.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val inputMethodManager: InputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    fun botonEliminar(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.delete_dialog_title))
            .setMessage("${resources.getString(R.string.delete_dialog_message)}:\n${localidad?.name}")
            .setNegativeButton(resources.getString(R.string.cancelar)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.acceptar)) { dialog, which ->
                localidad?.let { loc ->
                    this.localidadViewModel.deleteItem(loc.id)
                    this.localidadViewModel.getLocalidadesList()
                }
                finish()
            }
            .show()

    }

    fun botonAgregar(){

        val name = binding.ediTextLocalidadName.text.toString().trim()
        val lon = binding.ediTextLocalidadLon.text.toString().trim()
        val lat = binding.ediTextLocalidadLat.text.toString().trim()

        if (name.isEmpty() || lon.isEmpty() || lat.isEmpty() || lon.toDoubleOrNull()== null || lat.toDoubleOrNull()== null ) {
            Toast.makeText(this, getString(R.string.addLocationActivity_not_correct_data), Toast.LENGTH_LONG).show()
            finish()
        }
        if (localidad == null) {
            localidad = Localidad(0, "", "", "", "", 0.0, 0.0, "")
        }
        localidad?.name = binding.ediTextLocalidadName.text.toString()
        localidad?.description = binding.editTextLocalidadDescripcion.text.toString()
        localidad?.state = binding.ediTextLocalidadEstado.text.toString()
        localidad?.country = binding.ediTextLocalidadPais.text.toString()
        localidad?.latitude = binding.ediTextLocalidadLat.text.toString().toDouble()
        localidad?.longitude = binding.ediTextLocalidadLon.text.toString().toDouble()
        if (modoEdicion && localidad?.id != 0) {
            localidadViewModel.updateItem(localidad!!)
        }
        else{
            localidadViewModel.insertItem(localidad!!)
        }
        finish()
    }

    private fun llenarCampos(){
        if(modoEdicion) {
            binding.addLicationFragmentBtnEliminar.visibility = View.VISIBLE
            binding.addLicationFragmentBtnEliminar2.visibility = View.VISIBLE
            binding.addLicationFragmentBtnAgregar.text = getString(R.string.guardar)
            binding.extFab.text = getString(R.string.ubicar)
        }
        else {
            binding.addLicationFragmentBtnEliminar.visibility = View.GONE
            binding.addLicationFragmentBtnEliminar2.visibility = View.GONE
        }
        binding.ediTextLocalidadName.setText(localidad?.name)
        binding.editTextLocalidadDescripcion.setText(localidad?.description)
        binding.ediTextLocalidadEstado.setText(localidad?.state)
        binding.ediTextLocalidadPais.setText(localidad?.country)
        binding.ediTextLocalidadLat.setText(localidad?.latitude.toString())
        binding.ediTextLocalidadLon.setText(localidad?.longitude.toString())
    }

    private val getLocationFromMap = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            localidad = Constants.getSerializableCompat(activityResult.data, Constants.KEY_LOCALIDAD, Localidad::class.java)
            Log.d(Constants.LOGTAG, "Localidad de MAPS: ${localidad}")
            llenarCampos()
        }
    }
}