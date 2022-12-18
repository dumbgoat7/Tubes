package com.example.tubespbp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.Models.Vaccination
import kotlinx.android.synthetic.main.rv_vaccination_item.view.*

class VaccinationAdapter (private val vaccination: ArrayList<Vaccination>, private val listener : OnAdapterListener)
    : RecyclerView.Adapter<VaccinationAdapter.VaccinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccinationViewHolder {
        return VaccinationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_vaccination_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: VaccinationViewHolder, position: Int) {
        val vaccinate = vaccination[position]
        holder.view.tvProgram.text = vaccinate.program
        holder.view.tv_tglVaksin.text = vaccinate.tanggal
        holder.view.tv_pelaksana.text = vaccinate.pelaksana
        holder.view.cardVaccination.setOnClickListener{
            listener.onClick(vaccinate)
        }

    }

    override fun getItemCount(): Int {
        return vaccination.size
    }

    inner class VaccinationViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")

    fun setData(list: List<Vaccination>) {
        vaccination.clear()
        vaccination.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(note: Vaccination)
    }

}