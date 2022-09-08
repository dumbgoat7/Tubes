package com.example.tubespbp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StartUpActivity: AppCompatActivity() {
    private lateinit var logo : ImageView
    private lateinit var text : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        supportActionBar?.hide()

        val splash = AnimationUtils.loadAnimation(this, R.anim.splash)
        text = findViewById(R.id.judul)
        logo = findViewById(R.id.logo)
        logo.startAnimation(splash)
        text.startAnimation(splash)
        val moveHome = Intent(this@StartUpActivity, LoginActivity::class.java)

        Handler(Looper.getMainLooper()).postDelayed( {
            startActivity(moveHome)
            finish()
        }, 4000)

    }
}