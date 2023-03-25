package com.onc.ddm_proyectofinal_onc.view.activities

import com.onc.ddm_proyectofinal_onc.model.entities.Localidad

interface IRecyViewItemPressedListener {
    fun onItemClick(localidad: Localidad, index:Int)
    fun onItemLongClick(localidad: Localidad, index: Int)
}