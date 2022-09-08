package com.example.tubespbp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
//        val textDaftar: TextView = findViewById(R.id.Textdaftar)
        val btnLogin: Button = findViewById(R.id.btnLogin)

//        textDaftar.setOnClickListener(View.OnClickListener {
//            val moveHome = Intent(this@LoginActivity, RegisterActivity::class.java)
//            startActivity(moveHome)
//        })

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (username == "admin" && password == "0914") checkLogin = true
            if (!checkLogin) return@OnClickListener
//            val moveHome = Intent(this@LoginActivity, HomeActivity::class.java)
//            startActivity(moveHome)
        })


    }
}