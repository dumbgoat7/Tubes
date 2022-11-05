package com.example.tubespbp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.databinding.FragmentMainmenuBinding
import com.example.tubespbp.databinding.FragmentRsBinding
import com.example.tubespbp.entity.RumahSakit

class FragmentRS : Fragment(R.layout.fragment_rs) {
    private var _binding : FragmentRsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
//        this.FragmentActivity()?.setActionBar(R.layout.custom_action_bar)

        val listRS= arrayOf(
            RumahSakit("Rumah Sakit Panti Rapih","Jl. Cik Di Tiro No.30, Samirono, Terban, Kec. Gondokusuman, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55223","081813287817",R.drawable.hospital),
            RumahSakit("Rumah Sakit Panti Rini","Jl. Raya Solo - Yogyakarta KM.13,2, Kringinan, Tirtomartani, Kec. Kalasan, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55571","08919479336104",R.drawable.hospital),
            RumahSakit("RSPAU Dr. S. Hardjolukito","JL. Janti Yogyakarta, Lanud Adisutjipto, Jl. Ringroad Timur, Karang Janbe, Banguntapan, Kec. Banguntapan, Kabupaten Bantul, Daerah Istimewa Yogyakarta 55198","08603240654842",R.drawable.hospital),
            RumahSakit("Siloam Hospitals Yogyakarta","Jl. Laksda Adisucipto No.32-34, Demangan, Kec. Gondokusuman, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55221","08596559639066",R.drawable.hospital),
            RumahSakit("Rumah Sakit Bethesda","Jl. Jend. Sudirman No.70, Kotabaru, Kec. Gondokusuman, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55224","086031551967",R.drawable.hospital),
            RumahSakit("RS Bhayangkara Jogja","Jl. Raya Solo - Yogyakarta KM.14, Glondong, Tirtomartani, Kec. Kalasan, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55571","0816935390459",R.drawable.hospital),
            RumahSakit("Dr.Sardjito General Hospital","Jl. Kesehatan Jl. Kesehatan Sendowo No.1, Sendowo, Sinduadi, Kec. Mlati, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55281","088945415657",R.drawable.hospital),
            RumahSakit("Rumah Sakit Umum Queen Latifa","Jl. Ringroad Barat No.118, Mlangi, Nogotirto, Kec. Gamping, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55294","080876783617",R.drawable.hospital),
            RumahSakit("Hermina Hospital Yogya","Jl. Selokan Mataram, RT.06/RW.50, Meguwo, Maguwoharjo, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55282","084792101026", R.drawable.hospital),
            RumahSakit("JIH Hospital","Jl. Ring Road Utara No.160, Perumnas Condong Catur, Condongcatur, Kec. Depok, Kabupaten Sleman, Daerah Istimewa Yogyakarta 55283","084358242520",R.drawable.hospital)
        )

        val adapter : RVRumahSakitAdapter = RVRumahSakitAdapter(listRS)

        val rvRumahSakit: RecyclerView = view.findViewById(R.id.rv_rs)

        rvRumahSakit.layoutManager = layoutManager

        rvRumahSakit.setHasFixedSize(true)

        rvRumahSakit.adapter = adapter

        binding.ivMaps.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.layout_fragment, FragmentMaps())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


}