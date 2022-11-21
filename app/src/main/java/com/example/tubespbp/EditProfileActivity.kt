package com.example.tubespbp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.api.UserAPI
import com.example.tubespbp.databinding.ActivityEditProfileBinding
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    var itemBinding: ActivityEditProfileBinding? = null
    var sharedPreferences: SharedPreferences? = null
    private lateinit var editProfileLayout: FrameLayout
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        queue = Volley.newRequestQueue(applicationContext)

        setContentView(itemBinding?.root)

        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")
        val token = sharedPreferences?.getString("token", "")

//        itemBinding?.etName?.setText(db?.UserDao()?.getUser(id!!.toInt())?.username)
//        itemBinding?.etEmail?.setText(db?.UserDao()?.getUser(id!!.toInt())?.email)
//        itemBinding?.etTglLahir?.setText(db?.UserDao()?.getUser(id!!.toInt())?.tanggallahir)
//        itemBinding?.etNoHp?.setText(db?.UserDao()?.getUser(id!!.toInt())?.noHp)

        itemBinding?.etTglLahir?.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    itemBinding?.ilTglLahir?.editText?.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

                },
                year,
                month,
                day
            )

            dpd.show()
        }


        itemBinding?.btnSave?.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, LoginActivity::class.java)

            val Name: String = itemBinding?.ilName?.editText?.getText().toString()
            val Email: String = itemBinding?.ilEmail?.editText?.getText().toString()
            val TangalLahir: String = itemBinding?.etTglLahir?.getText().toString()
            val NoTelp: String = itemBinding?.ilNoHp?.editText?.getText().toString()

            var checkSave = true

            if (Name.isEmpty()) {
                itemBinding?.ilName?.setError("Name must be filled with text")
                checkSave = false
            }

            if (NoTelp.isEmpty()) {
                itemBinding?.ilNoHp?.setError("Phone Number must be filled with text")
                checkSave = false
            }

            if (Email.isEmpty()) {
                itemBinding?.ilEmail?.setError("E-mail must be filled with text")
                checkSave = false
            }

            if (!Email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                itemBinding?.ilEmail?.setError("Email tidak valid")
                checkSave = false
            }

            if (TangalLahir.isEmpty()) {
                itemBinding?.etTglLahir?.setError("Birth Date must be filled with text")
                checkSave = false
            }

            if (checkSave == true) {
                setupListener()
                Toast.makeText(
                    applicationContext,
                    "Your Profile Changed",
                    Toast.LENGTH_SHORT
                ).show()
                val moveMenu = Intent(this, HomeActivity::class.java)
                startActivity(moveMenu)
            } else {
                return@OnClickListener
            }
        })
    }


    private fun setupListener() {
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")

        db.UserDao().updateUser(
            User(
                id!!.toInt(),
                itemBinding?.etName?.getText().toString(),
                itemBinding?.etEmail?.text.toString(),
                itemBinding?.etTglLahir?.text.toString(),
                itemBinding?.etNoHp?.text.toString(),
                db?.UserDao()?.getUser(id!!.toInt())?.password.toString()
            )
        )
        finish()
    }

    private fun getUserById(id: Long, token:String){
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)

        val Username = itemBinding?.ilName?.editText
        val Email = itemBinding?.ilEmail?.editText
        val BirthDate = itemBinding?.etTglLahir
        val NoTelp = itemBinding?.ilNoHp?.editText

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, UserAPI.GET_BY_ID_URL + id,
                { response ->
                    val jsonObject = JSONObject(response)
                    val username = jsonObject.getJSONObject("data").getString("username")
                    val noTelp = jsonObject.getJSONObject("data").getString("notelp")
                    val email = jsonObject.getJSONObject("data").getString("email")
                    val birthdate = jsonObject.getJSONObject("data").getString("birthdate")

                    Username?.setText(username)
                    NoTelp?.setText(noTelp)
                    Email?.setText(email)
                    BirthDate?.setText(birthdate)

                },
                Response.ErrorListener{ error ->

                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun updateProfile(id: Long, token :String){
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)

        val Username = itemBinding?.ilName?.editText
        val Email = itemBinding?.ilEmail?.editText
        val BirthDate = itemBinding?.etTglLahir
        val NoTelp = itemBinding?.ilNoHp?.editText


        val user = com.example.tubespbp.Models.User(
            Username?.text.toString(),
            NoTelp?.text.toString(),
            Email?.text.toString(),
            BirthDate?.text.toString(),
            "1"
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.PUT, UserAPI.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var history = gson.fromJson(response, com.example.tubespbp.Models.User::class.java)

                if(history != null)
                    Toast.makeText(this@EditProfileActivity, "Data berhasil diubah", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()
            }, Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

}