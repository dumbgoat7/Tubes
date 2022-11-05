package com.example.tubespbp

import com.example.tubespbp.room.ModelMain
import kotlinx.android.synthetic.main.layout_tooltip.view.*
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class CustomInfoWindow(mapView: MapView?) : InfoWindow(R.layout.layout_tooltip, mapView) {
    override fun onClose(){

    }

    override fun onOpen(item: Any?) {
        val marker = item as Marker
        val infoWindowData = marker.relatedObject as ModelMain

        val tvNameLokasi = mView.tvNamaLokasi
        val tvAlamat = mView.tvAlamat
        val imageClose = mView.imageClose

        tvNameLokasi.text = infoWindowData.strName
        tvAlamat.text = infoWindowData.strVicinity
        imageClose.setOnClickListener {
            marker.closeInfoWindow()
        }
    }

}