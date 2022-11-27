package com.example.tubespbp

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isEmpty
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.Models.User
import com.example.tubespbp.api.UserAPI
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var loginActivity: ConstraintLayout

    lateinit var regUser: String
    lateinit var regPass: String

    private var queue: RequestQueue? = null

    var sharedPreferences: SharedPreferences? = null

    var mBundle: Bundle? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("token", "")
        println(token!!)
        queue = Volley.newRequestQueue(this)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        loginActivity = findViewById(R.id.loginactivity)
        val textDaftar: TextView = findViewById(R.id.Textdaftar)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        textDaftar.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val moveHome = Intent(this@LoginActivity, HomeActivity::class.java)
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

                if (username.isEmpty()){
                    inputUsername.setError("Username must be filled with text")
                    checkLogin = false
                }
                if(password.isEmpty()) {
                    inputPassword.setError("Password must be filled with text")
                    checkLogin = false
                }
                if(!inputUsername.isEmpty() && !inputPassword.isEmpty()){
                    checkLogin = true
                }

                if (!checkLogin) {
                    return@OnClickListener
                }else {
                    println("sudah masuk sini")

                    CoroutineScope(Dispatchers.IO).launch {
                        loginUser(inputUsername.editText?.text.toString(), inputPassword.editText?.text.toString())
                    }
                }
        })

        textDaftar.setOnClickListener(View.OnClickListener {
            val moveReg = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveReg)
        })

    }
    fun dialogBuilder(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ALERT")
        builder.setMessage("This Account doesn't exist!")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener{
                dialog, id -> loginActivity
        })
        builder.show()

    }

    fun getBundle() {
        mBundle = intent.getBundleExtra("register")!!
        regUser = mBundle!!.getString("username")!!
        regPass = mBundle!!.getString("password")!!
    }
    override fun onBackPressed() {
        return
    }

    private fun loginUser(username: String, password: String){

        println("sudah masuk sini 2")
        println(username)
        println(password)

        val stringRequest: StringRequest =
            object: StringRequest(Method.GET, UserAPI.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val id = jsonObject.getJSONArray("data").toString()
                var user : Array<User> = gson.fromJson(id, Array<User>::class.java)
                for (i in user){
                    if (i.username == username && i.password==password){
                        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
                        var editor = sharedPreferences?.edit()
                        println(id)
                        editor?.putString("id", i.id.toString())
                        editor?.commit()
                        Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT)
                            .show()
                        val moveMenu = Intent(this, HomeActivity::class.java)
                        startActivity(moveMenu)
                    }
                }

                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT)
                    .show()
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
//                AlertDialog.Builder(applicationContext)
//                    .setTitle("Error")
//                    .setMessage(error.message)
//                    .setPositiveButton("OK", null)
//                    .show()
                Toast.makeText(this@LoginActivity, "Username/Password incorrect", Toast.LENGTH_SHORT)
                    .show()
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    println(headers)
                    return headers
                }

                //                @Throws(AuthFailureError::class)
//                override fun getBody(): ByteArray {
//                    val gson = Gson()`
//                    val requestBody = gson.toJson(profile)
//                    return requestBody.toByteArray(StandardCharsets.UTF_8)
//                }
                override fun getParams(): MutableMap<String, String>?{
                    val params = HashMap<String, String>()
                    params["username"] = username
                    params["password"] = password
                    return params
                }

//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
            }
        queue!!.add(stringRequest)
    }
}