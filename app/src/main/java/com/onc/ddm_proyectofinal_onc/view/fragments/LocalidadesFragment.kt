package com.onc.ddm_proyectofinal_onc.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onc.ddm_proyectofinal_onc.R
import com.onc.ddm_proyectofinal_onc.databinding.FragmentLocalidadesBinding
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.utils.Constants
import com.onc.ddm_proyectofinal_onc.view.activities.AddLocationsActivity
import com.onc.ddm_proyectofinal_onc.view.activities.IRecyViewItemPressedListener
import com.onc.ddm_proyectofinal_onc.view.activities.MainActivity
import com.onc.ddm_proyectofinal_onc.view.adapters.LocalidadesRecyViewAdapater
import com.onc.ddm_proyectofinal_onc.viewmodel.LocalidadesListViewModel

class LocalidadesFragment : Fragment() {

    private  var _binding: FragmentLocalidadesBinding? = null
    private val binding get() = _binding!!
    private val localidadesViewModel : LocalidadesListViewModel by viewModels()


    //Adaptador del RecycleView
    private lateinit var adapter : LocalidadesRecyViewAdapater
   // private val  selectedLocalidades = ArrayList<Localidad>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocalidadesBinding.inflate(inflater, container, false)
        val view  = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LocalidadesRecyViewAdapater(arrayListOf())
        adapter.clickEvents = object : IRecyViewItemPressedListener {
            override fun onItemClick(localidad: Localidad, index: Int) {
                //Toast.makeText(this@LocalidadesFragment.requireContext(), "Evento Click", Toast.LENGTH_LONG).show()

                //Se abre la actividad AddLocation prellenada
                val intent = Intent(this@LocalidadesFragment.requireContext(), AddLocationsActivity::class.java)
                intent.putExtra(Constants.KEY_LOCALIDAD, localidad)
                startActivity(intent)
            }

            override fun onItemLongClick(localidad: Localidad, index: Int) {

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.delete_dialog_title))
                    .setMessage("${resources.getString(R.string.delete_dialog_message)}: ${localidad.name}")
                    .setNegativeButton(resources.getString(R.string.cancelar)) { dialog, which ->
                        // Respond to negative button press
                    }
                    .setPositiveButton(resources.getString(R.string.acceptar)) { dialog, which ->
                        localidadesViewModel.deleteItem(localidad.id)
                        localidadesViewModel.getLocalidadesList()
                    }
                    .show()

                //Toast.makeText(this@LocalidadesFragment.requireContext(), "Evento LONG Click", Toast.LENGTH_LONG).show()
                /*if (selectedLocalidades.contains(localidad))
                {
                    selectedLocalidades.remove(localidad)
                }
                else {
                    selectedLocalidades.add(localidad)
                }

                 */

            }

        }
        //Configuración del RecycleView
        binding.rvListaLocalidades.itemAnimator = DefaultItemAnimator()
        binding.rvListaLocalidades.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvListaLocalidades.adapter = adapter

        //Establece el observador para el cambio en el repositorio
        localidadesViewModel.localidadesList.observe(viewLifecycleOwner, Observer {
            adapter.updateItems(it)
        })

        localidadesViewModel.getLocalidadesList()

       // configContextMenu()

    }

    /*
    private fun configContextMenu(){

        val actionModeCallback = object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                this@LocalidadesFragment.activity?.menuInflater?.inflate(R.menu.menu_contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.delete -> {
                        Toast.makeText(requireContext(), "Se han seleccionado: " + selectedLocalidades.size, Toast.LENGTH_LONG).show()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }

        var activity = this.activity as MainActivity
        val actionMode = activity.startSupportActionMode(actionModeCallback)
        actionMode?.title = "${selectedLocalidades.size} Seleccionas..."
    }
*/

    override fun onStart() {
        super.onStart()
        localidadesViewModel.getLocalidadesList()

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}