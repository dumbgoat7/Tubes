package com.example.tubespbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isEmpty
import com.google.android.material.textfield.TextInputLayout
import java.util.ResourceBundle.getBundle

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mBundle : Bundle
    private lateinit var username : String
    private lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
//        getBundle()


        inputUsername = findViewById(R.id.inputLayoutUsername)
//        inputUsername.editText?.setText(username)

        inputPassword = findViewById(R.id.inputLayoutPassword)
//        inputPassword.editText?.setText(password)

        val textDaftar: TextView = findViewById(R.id.Textdaftar)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        textDaftar.setOnClickListener(View.OnClickListener {
            val moveHome = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveHome)
        })

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false

            if (inputUsername.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (inputPassword.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (username == "raxx" && password == "xxxx") checkLogin = true
//            if (!checkLogin) return@OnClickListener
//            val moveHome = Intent(this@LoginActivity, HomeActivity::class.java)
//            startActivity(moveHome)
        })
//        fun alertDialog(val username: String, val password: String){
//            val builder = AlertDialog.Builder(this)
//
//
//        }



    }
        fun getBundle() {
            mBundle = intent.getBundleExtra("register")!!

            username = mBundle.getString("username")!!
            password = mBundle.getString("password")!!
        }
}