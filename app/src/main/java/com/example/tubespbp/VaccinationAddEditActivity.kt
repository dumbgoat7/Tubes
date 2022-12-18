package com.example.tubespbp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.Models.News
import com.example.tubespbp.Models.Vaccination
import com.example.tubespbp.api.NewsAPI
import com.example.tubespbp.api.UserAPI
import com.example.tubespbp.api.VaccinationAPI
import com.example.tubespbp.databinding.ActivityAddeditVaccinationBinding
import com.example.tubespbp.room.Constant
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_addedit_vaccination.*
import kotlinx.android.synthetic.main.activity_edit_news.*
import kotlinx.android.synthetic.main.fragment_vaccination.*
import kotlinx.android.synthetic.main.rv_vaccination_item.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class VaccinationAddEditActivity : AppCompatActivity() {
    private var queue: RequestQueue? = null
    var sharedPreferences: SharedPreferences? = null
    private var VaccinationId : Int = 0
    private lateinit var binding: ActivityAddeditVaccinationBinding
    private var UserId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddeditVaccinationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        queue = Volley.newRequestQueue(this)
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        VaccinationId = intent.getIntExtra("intent_id",0)

        binding.inputTanggalVaksin.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this, { view, year, monthOfYear, dayOfMonth ->
                    binding.inputTanggalVaksin.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)
                }, year, month, day)
            datePicker.show()
        }

        val intentType = intent.getIntExtra("intent_type",1)
//        val intentType = 1
        when (intentType) {
            Constant.TYPE_CREATE -> {
                fabUpdate.visibility = View.GONE
                fabDelete.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                val username = sharedPreferences?.getString("username", "")
                if(username == "admin") {
                    fabSave.visibility = View.GONE
                } else {
                    fabSave.visibility = View.GONE
                    fabUpdate.visibility = View.GONE
                    fabDelete.visibility = View.GONE
                    inputProgram.keyListener = null
                    inputEmail.keyListener = null
                    inputTanggalVaksin.keyListener = null
                    inputPelaksana.keyListener = null
                }
                getVaccination(VaccinationId)
            }
        }

        fabSave.setOnClickListener {
            createVaccination()
        }
        fabUpdate.setOnClickListener{
            updateVaccination(VaccinationId)
        }
        fabDelete.setOnClickListener{
            deleteVaccination(VaccinationId)
        }
    }
    private fun getVaccination(id: Int){

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, VaccinationAPI.GET_BY_ID_URL + id,
                { response ->
                    val jsonObject = JSONObject(response)
                    val Program = jsonObject.getJSONObject("data").getString("program")
                    val Tanggal = jsonObject.getJSONObject("data").getString("tanggal")
                    val Pelaksana = jsonObject.getJSONObject("data").getString("pelaksana")
                    val idUser = jsonObject.getJSONObject("data").getString("idUser").toInt()
                    inputProgram.setText(Program)
                    inputTanggalVaksin.setText(Tanggal)
                    inputPelaksana.setText(Pelaksana)
                    getUserbyId(idUser)
                },
                Response.ErrorListener{ error ->

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

    private fun getUserbyId(id: Int){

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, UserAPI.GET_BY_ID_URL + id,
                { response ->
                    val jsonObject = JSONObject(response)
                    val Email = jsonObject.getJSONObject("data").getString("email")
                    inputEmail!!.setText(Email)

                },
                Response.ErrorListener{ error ->

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
    private fun getUserbyEmail(email: String){

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, UserAPI.GET_BY_ID_URL + email,
                { response ->
                    val jsonObject = JSONObject(response)
                    val id = jsonObject.getJSONObject("data").getInt("id")
                    UserId = id

                },
                Response.ErrorListener{ error ->

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

    private fun createVaccination() {
        val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(binding?.inputTanggalVaksin?.toString(), formatter)
        getUserbyEmail(binding.inputEmail.text.toString())

        val vaccine = Vaccination(
            binding.inputProgram.text.toString(),
            UserId,
            date.toString(),
            binding.inputPelaksana.text.toString()
        )
        Log.d("add",vaccine.toString())
        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, VaccinationAPI.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var addVaccine = gson.fromJson(response, Vaccination::class.java)

                if (addVaccine != null) {
                    Toast.makeText(this@VaccinationAddEditActivity, "Successfully Adding Vaccination Certificate", Toast.LENGTH_SHORT).show()
                }
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()
            }, Response.ErrorListener { error ->
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@VaccinationAddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    println(headers)
                    return headers
                }
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["program"] = inputProgram.text.toString()
                    params["idUser"] = UserId.toString()
                    params["tanggal"] = inputTanggalVaksin.text.toString()
                    params["pelaksana"] = inputPelaksana.text.toString()
                    return params
                }
            }

        queue!!.add(stringRequest)
    }


    private fun updateVaccination(id: Int) {
        val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(binding?.inputTanggalVaksin?.toString(), formatter)
        getUserbyEmail(binding.inputEmail.text.toString())

        val vaccine = Vaccination(
            binding.inputProgram.text.toString(),
            UserId,
            date.toString(),
            binding.inputPelaksana.text.toString()
        )
        val stringRequest: StringRequest =
            object :
                StringRequest(Method.PUT, VaccinationAPI.UPDATE_URL + id, Response.Listener { response ->
                    val gson = Gson()
                    var updateVaccine = gson.fromJson(response, Vaccination::class.java)

                    if (updateVaccine != null) {
                        Toast.makeText(this@VaccinationAddEditActivity, "Successfully Update Vaccination Certificate", Toast.LENGTH_SHORT).show()

                    }
                    finish()
                }, Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@VaccinationAddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["program"] = inputProgram.text.toString()
                    params["idUser"] = UserId.toString()
                    params["tanggal"] = inputTanggalVaksin.text.toString()
                    params["pelaksana"] = inputPelaksana.text.toString()
                    return params
                }
            }

        queue!!.add(stringRequest)
    }

    private fun deleteVaccination(id: Int) {
        val stringRequest : StringRequest = object :
            StringRequest(Method.DELETE, VaccinationAPI.DELETE_URL + id, Response.Listener {response ->
                val gson = Gson()
                var news = gson.fromJson(response, News::class.java)
                if (news != null)
                    Toast.makeText(this@VaccinationAddEditActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@VaccinationAddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@VaccinationAddEditActivity, e.message, Toast.LENGTH_SHORT).show()
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