package com.example.tubespbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputUsername : TextInputLayout
    private lateinit var inputEmail : TextInputLayout
    private lateinit var inputTanggalLahir : TextInputLayout
    private lateinit var inputNoHp : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    private lateinit var registerActivity: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("Daftar")

        inputUsername = findViewById(R.id.layoutUsername)
        inputEmail = findViewById(R.id.layoutEmail)
        inputTanggalLahir = findViewById(R.id.layoutTanggalLahir)
        inputNoHp = findViewById(R.id.layoutNoHp)
        inputPassword = findViewById(R.id.layoutPassword)
        registerActivity = findViewById(R.id.registerActivity)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            var checkRegister = true
            val username: String = inputUsername.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val tanggalLahir: String = inputTanggalLahir.getEditText()?.getText().toString()
            val noHp: String = inputNoHp.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkRegister = false
            }

            if (email.isEmpty()){
                inputEmail.setError("Email must be filled with text")
                checkRegister = false
            }

            if (tanggalLahir.isEmpty()){
                inputTanggalLahir.setError("Tanggal lahir must be filled with text")
                checkRegister = false
            }

            if (noHp.isEmpty()){
                inputNoHp.setError("Nomor Hp must be filled with text")
                checkRegister = false
            }

            if (password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkRegister = false
            }

            if(!checkRegister)return@OnClickListener
            val moveLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(moveLogin)

            mBundle.putString("username" , inputUsername.getEditText()?.getText().toString())
            mBundle.putString("email" , inputEmail.getEditText()?.getText().toString())
            mBundle.putString("tanggal Lahir" , inputTanggalLahir.getEditText()?.getText().toString())
            mBundle.putString("noHp" , inputNoHp.getEditText()?.getText().toString())
            mBundle.putString("password" , inputPassword.getEditText()?.getText().toString())
            Snackbar.make(registerActivity, "Akun Berhasil dibuat!", Snackbar.LENGTH_LONG).show()

            intent.putExtra("register",mBundle)
            startActivity(intent)
        })
    }
}