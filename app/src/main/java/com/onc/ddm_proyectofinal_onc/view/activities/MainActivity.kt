package com.onc.ddm_proyectofinal_onc.view.activities

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.databinding.ActivityMainBinding
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.utils.Constants
import com.onc.ddm_proyectofinal_onc.utils.ManejadorPermisos
import com.onc.ddm_proyectofinal_onc.view.adapters.LocalidadesRecyViewAdapater
import com.onc.ddm_proyectofinal_onc.view.adapters.NavRecyViewAdapater
import com.onc.ddm_proyectofinal_onc.view.adapters.ViewPageStateAdapter
import com.onc.ddm_proyectofinal_onc.view.fragments.WeekFragment
import com.onc.ddm_proyectofinal_onc.viewmodel.LocalidadSelectedViewModel
import com.onc.ddm_proyectofinal_onc.viewmodel.LocalidadesListViewModel
import com.onc.ddm_proyectofinal_onc.viewmodel.WeatherForecastViewModel


class MainActivity : AppCompatActivity() {

    var localidadActual: Localidad =  Localidad(0, "Ciudad Universitaria",
        "C.U., Ciudad de México", "Ciudad de México", "México",
        19.31888949, -99.1843676, "")

    val localidadSelectedViewmodel: LocalidadSelectedViewModel by viewModels()

    private val localidadesViewModel : LocalidadesListViewModel by viewModels()


    //private val weatherForecastViewModel: WeatherForecastViewModel by viewModels()

    //Adaptador del RecycleView
    private lateinit var adapterNav : NavRecyViewAdapater

    private val adapter by lazy {ViewPageStateAdapter(this,localidadActual)}
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)

        binding.toolbarMain.setNavigationOnClickListener {
            //.makeText(this, "Se ha hecho clic en el toolbar", Toast.LENGTH_LONG).show()
            binding.drawerLayout.open()
        }

        configViewPager()
        configTabLayout()
        configFAB()
        configNavMenu()

          //Se registra la actividad para observar a la localidad seleccionada
        val localidadObserver = Observer<Localidad> {
            this.localidadActual = it
        }
        localidadSelectedViewmodel.localidadLiveData.observe(this, localidadObserver)


    }


    override fun onStart() {
        super.onStart()

        localidadesViewModel.getLocalidadesList()

    }

    fun configNavMenu(){
        adapterNav =  NavRecyViewAdapater(arrayListOf())
        adapterNav.clickEvents = object : IRecyViewItemPressedListener {
            override fun onItemClick(localidad: Localidad, index: Int) {
               // localidadActual = localidad
               // Log.d(Constants.LOGTAG, "PAcketNAme ${this@MainActivity.applicationContext.packageName}")
                this@MainActivity.localidadSelectedViewmodel.setLocalidadSelected(localidad)
                //Toast.makeText(this@MainActivity, "Se ha LONG click  ${localidadActual.name}", Toast.LENGTH_LONG).show()
                binding.drawerLayout.closeDrawers()

            }

            override fun onItemLongClick(localidad: Localidad, index: Int) {

                this@MainActivity.localidadSelectedViewmodel.setLocalidadSelected(localidad)
                //Toast.makeText(this@MainActivity, "Se ha LONG click  ${localidadActual.name}", Toast.LENGTH_LONG).show()
                //binding.drawerLayout.closeDrawers()

            }

        }
        //Configuración del RecycleView

        val header = binding.navigationView.getHeaderView(0)
        val recyViewv = header.findViewById<RecyclerView>(R.id.nav_recycleView)

        recyViewv.itemAnimator = DefaultItemAnimator()
        recyViewv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyViewv.adapter = adapterNav

        //Establece el observador para el cambio en el repositorio

        localidadesViewModel.localidadesList.observe(this
            , Observer {
            adapterNav.updateItems(it)
        })

        localidadesViewModel.getLocalidadesList()
    }



    private fun configFAB() {
        binding.fbtnAgregar.setOnClickListener {

            //Se abre la actividad AddLocation
            val intent = Intent(this, AddLocationsActivity::class.java)
            startActivity(intent)
            /*
            supportFragmentManager.beginTransaction()
                .replace(binding.addLocationFrameLayout.id, AddLocalidadFragment(), "AddLocalidadFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()

             */
        }
    }

    //--------------------------------------------------------------------------
    //Métodos TabLayout y ViewPage
    private fun configTabLayout() {
       TabLayoutMediator( binding.tablayout, binding.viewPager){
           tab, position ->

           when(position){
               0 -> { tab.text = getString(R.string.main_tab_today) }
               1 -> { tab.text = getString(R.string.main_tab_diary) }
               //2 -> { tab.text = getString(R.string.main_tab_week) }
               2 -> { tab.text = getString(R.string.main_tab_Localidades) }
           }
       }.attach()
    }

    private fun configViewPager() {
        val adapter = ViewPageStateAdapter(this, localidadActual)
        binding.viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        val viewPager = binding.viewPager
        if(viewPager.currentItem == 0){
            super.onBackPressed()
        }
        else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onStop() {
        super.onStop()
        localidadSelectedViewmodel.saveLocalidadSelected()
    }



}
