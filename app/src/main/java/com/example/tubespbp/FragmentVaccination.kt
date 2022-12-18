package com.example.tubespbp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.Models.Vaccination
import com.example.tubespbp.api.VaccinationAPI
import com.example.tubespbp.databinding.FragmentVaccinationBinding
import com.example.tubespbp.room.Constant
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_vaccination.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.HashMap

class FragmentVaccination : Fragment() {
    lateinit var vaccinationAdapter: VaccinationAdapter

    private var queue: RequestQueue? = null
    var sharedPreferences: SharedPreferences? = null
    private var _binding : FragmentVaccinationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {

        _binding = FragmentVaccinationBinding.inflate(inflater, container, false)
        val rootView = binding.root

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
        vaccinationAdapter = VaccinationAdapter(arrayListOf(), object : VaccinationAdapter.OnAdapterListener {
            override fun onClick(vaccination: Vaccination) {
                vaccination.id?.let { it-> intentEdit(it, Constant.TYPE_READ) }
            }
        })
        rv_vaccine.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = vaccinationAdapter
        }
        loadData()
        setupListener()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            allVaccination()
        }
    }

    fun setupListener() {
        addVaccination.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(vaccinationId : Int, intentType: Int){
        startActivity(
            Intent(activity?.applicationContext, VaccinationAddEditActivity::class.java)
                .putExtra("intent_id", vaccinationId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun allVaccination() {
        val stringRequest : StringRequest = object :
            StringRequest(Method.GET, VaccinationAPI.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                var vaccination : Array<Vaccination> = gson.fromJson(jsonObject.getJSONArray("data").toString(), Array<Vaccination>::class.java)
                vaccinationAdapter.setData(vaccination.toList())
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        requireContext(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
}