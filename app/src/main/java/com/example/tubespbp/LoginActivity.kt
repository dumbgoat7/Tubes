package com.example.tubespbp

import android.content.DialogInterface
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
import android.provider.ContactsContract
import com.google.android.material.snackbar.Snackbar
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB


class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var loginActivity: ConstraintLayout
    private lateinit var mBundle : Bundle

    lateinit var regUser : String
    lateinit var regPass : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val textDaftar: TextView = findViewById(R.id.Textdaftar)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        loginActivity = findViewById(R.id.loginactivity)

        textDaftar.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = true
            if(!regUser.isEmpty() && !regPass.isEmpty()){

                val username: String = inputUsername.getEditText()?.getText().toString()
                val password: String = inputPassword.getEditText()?.getText().toString()

                if (username.isEmpty() && password.isEmpty()) {
                    inputUsername.setError("Username must be filled with text")
                    inputPassword.setError("Password must be filled with text")
                    var checkLogin = false
                } else if(password.length < 8){
                    inputPassword.setError("Password should more than contain 8 character")
                } else{
                    checkLogin = true
                }
                if (!checkLogin) return@OnClickListener

            val moveHome = Intent(this@LoginActivity, HomeActivity::class.java)
                finish()
                startActivity(moveHome)
            } else {
                dialogBuilder()
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
            builder.setMessage("Please register yourself!")
            builder.setPositiveButton("OK", DialogInterface.OnClickListener{
                    dialog, id -> loginActivity
            })
            builder.show()

        }
        fun setText() {
            inputUsername.getEditText()?.setText(regUser)
            inputPassword.getEditText()?.setText(regPass)
        }
        fun getBundle() {
        mBundle = intent.getBundleExtra("register")!!
        regUser = mBundle.getString("username")!!
        regPass = mBundle.getString("password")!!
    }
}