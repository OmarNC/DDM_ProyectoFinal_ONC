package com.onc.ddm_proyectofinal_onc.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onc.ddm_proyectofinal_onc.databinding.RecycleviewItemLocalidadBinding
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.view.activities.IRecyViewItemPressedListener

class LocalidadesRecyViewAdapater (private  val items : ArrayList<Localidad>): RecyclerView.Adapter<LocalidadesRecyViewAdapater.LocalidadViewHolder>() {


    var clickEvents: IRecyViewItemPressedListener?= null

    class LocalidadViewHolder(val binding: RecycleviewItemLocalidadBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(localidad : Localidad, index: Int, clickEvent: IRecyViewItemPressedListener?){


            binding.textViewName.text = localidad.name

            binding.textViewLocalidadId.text = localidad.id.toString()
            binding.textViewLocalidadName.text = localidad.name
            binding.textViewLocalidadDescripcion.text = localidad.description
            binding.textViewLocalidadEstado.text = localidad.state
            binding.textViewLocalidadPais.text = localidad.country
            binding.textViewLocalidadLatitud.text = localidad.latitude.toString()
            binding.textViewLocalidadLongitud.text = localidad.longitude.toString()

            binding.chBxDetalle.setOnCheckedChangeListener { boton, isChecked ->
                if (isChecked) {
                    binding.cardGroup.visibility = View.VISIBLE
                } else {
                    binding.cardGroup.visibility = View.GONE

                }
            }


            binding.idCard.setOnClickListener {
                clickEvent?.onItemClick(localidad, index)
            }

            binding.idCard.setOnLongClickListener {
                clickEvent?.onItemLongClick(localidad, index)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalidadViewHolder {
        val binding = RecycleviewItemLocalidadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocalidadViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: LocalidadViewHolder, position: Int) {
        holder.bind(items[position], position, clickEvents)
    }

    fun updateItems(newItems: ArrayList<Localidad>)
    {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}