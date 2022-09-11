package com.example.tubespbp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.entity.RumahSakit

class FragmentRS : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rs, container, false)
    }
    override fun
            onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        val listRS= arrayOf(
            RumahSakit("Panti Rapih","Yogakarta","081813287817",R.id.iv_gambar),
            RumahSakit("Panti Rini","Yogakarta","08919479336104",R.id.iv_gambar),
            RumahSakit("HarjoLukito","Yogakarta","08603240654842",R.id.iv_gambar),
            RumahSakit("Siloam Yogyakarta","Yogakarta","08596559639066",R.id.iv_gambar),
            RumahSakit("Bethesda","Yogyakarta","086031551967",R.id.iv_gambar),
            RumahSakit("Bhayangkara","Yogakarta","0816935390459",R.id.iv_gambar),
            RumahSakit("Sardjito","Yogakarta","088945415657",R.id.iv_gambar),
            RumahSakit("RS Umum Queen Latifa","Yogakarta","080876783617",R.id.iv_gambar),
            RumahSakit("Hermina","Yogyakarta","084792101026",R.id.iv_gambar),
            RumahSakit("RS Khusus Bedah Sinduadi","Yogakarta","084358242520",R.id.iv_gambar)
        )

        val adapter : RVRumahSakitAdapter = RVRumahSakitAdapter(listRS)

        val rvRumahSakit: RecyclerView = view.findViewById(R.id.rv_rs)

        rvRumahSakit.layoutManager = layoutManager

        rvRumahSakit.setHasFixedSize(true)

        rvRumahSakit.adapter = adapter
    }


}