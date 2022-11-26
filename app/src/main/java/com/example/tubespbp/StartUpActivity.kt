package com.example.tubespbp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StartUpActivity : AppCompatActivity() {
    private lateinit var logo : ImageView
    private lateinit var text : TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("first_time", Context.MODE_PRIVATE)
//        val isFirstTimeOpened = sharedPreferences.getBoolean("first_time", true)
        val isFirstTimeOpened = true

        if(isFirstTimeOpened) {
            setContentView(R.layout.activity_startup)
            supportActionBar?.hide()
            val splash = AnimationUtils.loadAnimation(this, R.anim.splash)
            text = findViewById(R.id.judul)
            logo = findViewById(R.id.logo)
            logo.startAnimation(splash)
            text.startAnimation(splash)

//            sharedPreferences
//                .edit()
//                .putBoolean("first_time", false)
//                .apply()

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, IntroPageActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}