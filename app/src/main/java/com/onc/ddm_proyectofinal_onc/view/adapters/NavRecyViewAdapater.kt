package com.onc.ddm_proyectofinal_onc.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onc.ddm_proyectofinal_onc.databinding.NavRecycleviewItemBinding
import com.onc.ddm_proyectofinal_onc.databinding.RecycleviewItemLocalidadBinding
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.view.activities.IRecyViewItemPressedListener

class NavRecyViewAdapater (private  val items : ArrayList<Localidad>): RecyclerView.Adapter<NavRecyViewAdapater.NavViewHolder>() {


    //Prueba
    //val localidad = Localidad(0, "Nombre", "descripcion", "Edo", "MEx", 0.0, 0.0, "")


    var clickEvents: IRecyViewItemPressedListener?= null

    class NavViewHolder(val binding: NavRecycleviewItemBinding): RecyclerView.ViewHolder(binding.root){

        companion object{
            var selectedCard: Int = -1  //Guarda la CardView Seleccionada --Genera mutua exclusion para la selección
        }

        fun bind(localidad : Localidad, index: Int, clickEvent: IRecyViewItemPressedListener?){
            binding.textViewName.text = localidad.name
            binding.textViewLocalidadDescripcion.text = localidad.description
            binding.idCard.isChecked = (index == selectedCard)

            binding.idCard.setOnClickListener {

                if (index != selectedCard) {
                    this.bindingAdapter?.notifyItemChanged(selectedCard)
                    selectedCard = index
                }
                binding.idCard.setChecked(!binding.idCard.isChecked)

                clickEvent?.onItemClick(localidad, index)
            }

            binding.idCard.setOnLongClickListener {

                if (index != selectedCard) {
                    this.bindingAdapter?.notifyItemChanged(selectedCard)
                    selectedCard = index
                }
                binding.idCard.setChecked(!binding.idCard.isChecked)

                clickEvent?.onItemLongClick(localidad, index)
                return@setOnLongClickListener true
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        val binding = NavRecycleviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NavViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {
        holder.bind(items[position], position, clickEvents)
    }



    fun updateItems(newItems: ArrayList<Localidad>)
    {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}