package com.onc.ddm_proyectofinal_onc.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onc.ddm_proyectofinal_onc.model.entities.Localidad
import com.onc.ddm_proyectofinal_onc.view.fragments.LocalidadesFragment
import com.onc.ddm_proyectofinal_onc.view.fragments.DiaryFragment
import com.onc.ddm_proyectofinal_onc.view.fragments.TodayFragment
import com.onc.ddm_proyectofinal_onc.view.fragments.WeekFragment

class ViewPageStateAdapter(fragment: FragmentActivity,  val localidadActual: Localidad) : FragmentStateAdapter(fragment) {

    private val NUM_FRAGMENTOS_PAGINAS = 3

    override fun getItemCount(): Int = NUM_FRAGMENTOS_PAGINAS

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when(position)  {
            0 -> { fragment = TodayFragment.newInstance(this.localidadActual) }
            1 -> { fragment = DiaryFragment.newInstance(this.localidadActual) }
            //2 -> { fragment = WeekFragment.newInstance(this.localidadActual) }
            2 -> { fragment = LocalidadesFragment() }
        }
        return fragment
    }
}