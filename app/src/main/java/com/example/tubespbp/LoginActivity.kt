package com.example.tubespbp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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

            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            val UserDB: User? = db.UserDao().getLogin(inputUsername.editText?.getText().toString(), inputPassword.editText?. getText(). toString())

            if(!inputUsername.isEmpty() && !inputPassword.isEmpty()){

                val username: String = inputUsername.getEditText()?.getText().toString()
                val password: String = inputPassword.getEditText()?.getText().toString()

                if (username.isEmpty() && password.isEmpty()) {
                    inputUsername.setError("Username must be filled with text")
                    inputPassword.setError("Password must be filled with text")
                    checkLogin = false
                }
                if(password.length < 8){
                    inputPassword.setError("Password should more than contain 8 character")
                }
                if(mBundle != null){
                    getBundle()
                    if(username == regUser && password == regPass){
                        checkLogin = true
                    }
                }
                if (!checkLogin) return@OnClickListener
                sharedPreferences = this.getSharedPreferences("userlog", Context.MODE_PRIVATE)
                var editor = sharedPreferences?.edit()
                editor?.putString("id", UserDB?.id.toString())
                editor?.commit()

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
        fun getBundle() {
        mBundle = intent.getBundleExtra("register")!!
        regUser = mBundle!!.getString("username")!!
        regPass = mBundle!!.getString("password")!!
    }
}