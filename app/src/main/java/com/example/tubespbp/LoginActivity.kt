package com.example.tubespbp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isEmpty
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var loginActivity: ConstraintLayout

    lateinit var regUser: String
    lateinit var regPass: String

    var sharedPreferences: SharedPreferences? = null

    var mBundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

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
                    val userDB: User? = db.UserDao().getLogin(username, password)

                    CoroutineScope(Dispatchers.IO).launch {
                        if (userDB != null) {
                            Log.d("MainActivity", "dbResponse: $userDB")
                            withContext(Dispatchers.Main) {

                                sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                                var editor = sharedPreferences?.edit()
                                editor?.putString("id", userDB?.id.toString())
                                editor?.commit()

                                startActivity(moveHome)
                            }
                        } else {
                            Log.d("MainActivity", "Account doesn't exist")
                            withContext(Dispatchers.Main){
                                dialogBuilder()
                            }
                        }
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
}