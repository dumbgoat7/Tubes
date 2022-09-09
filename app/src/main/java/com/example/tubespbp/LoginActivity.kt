package com.example.tubespbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

//        inputUsername = findViewById(R.id.inputLayoutUsername)
//        inputPassword = findViewById(R.id.inputLayoutPassword)
        val textDaftar: TextView = findViewById(R.id.Textdaftar)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        textDaftar.setOnClickListener(View.OnClickListener {
            val moveHome = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveHome)
        })

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val bundle = intent.extras
            val username: String = bundle!!.getString("username","")
            val password: String = bundle!!.getString("password","")


//            inputUsername?.setText(!!)

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (username == "admin" && password == "xxxx") checkLogin = true
            if (!checkLogin) return@OnClickListener
//            val moveHome = Intent(this@LoginActivity, HomeActivity::class.java)
//            startActivity(moveHome)
        })
//        fun alertDialog(val username: String, val password: String){
//            val builder = AlertDialog.Builder(this)
//
//
//        }


    }
}