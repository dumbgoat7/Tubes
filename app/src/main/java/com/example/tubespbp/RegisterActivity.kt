package com.example.tubespbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var username : TextInputLayout
    private lateinit var email : TextInputLayout
    private lateinit var noHp : TextInputLayout
    private lateinit var password : TextInputLayout
    private lateinit var registerActivity: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("Daftar")

        username = findViewById(R.id.layoutUsername)
        email = findViewById(R.id.layoutEmail)
        noHp = findViewById(R.id.layoutNoHp)
        password = findViewById(R.id.layoutPassword)
        registerActivity = findViewById(R.id.registerActivity)

        val btnRegister: Button = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            mBundle.putString("username" , username.editText.toString())
            mBundle.putString("email" , email.editText.toString())
            mBundle.putString("noHp" , noHp.editText.toString())
            mBundle.putString("password" , password.editText.toString())
            Snackbar.make(registerActivity, "Akun Berhasil dibuat!", Snackbar.LENGTH_LONG).show()

            intent.putExtra("register",mBundle)
            startActivity(intent)
        }

    }
}