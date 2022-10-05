package com.example.tubespbp

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class IntroPageActivity  : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intropage)
        supportActionBar?.hide()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_introfragment, IntroPage1Fragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}