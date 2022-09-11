package com.example.tubespbp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.entity.RumahSakit

class RVRumahSakitAdapter(private val data:Array<RumahSakit>) : RecyclerView.Adapter<RVRumahSakitAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_rs, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvNamaRS.text = currentItem.name
        holder.tvAlamat.text = currentItem.Alamat
        holder.tvNotelp.text = currentItem.noTelp
        holder.ivgambar.setImageResource(currentItem.photo)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaRS :TextView = itemView.findViewById(R.id.tv_nama_rs)
        val tvAlamat : TextView = itemView.findViewById(R.id.tv_alamat)
        val tvNotelp  : TextView = itemView.findViewById(R.id.tv_noTelp)
        val ivgambar : ImageView = itemView.findViewById(R.id.iv_gambar)


    }
}