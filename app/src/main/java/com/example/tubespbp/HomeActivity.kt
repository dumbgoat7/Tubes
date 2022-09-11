package com.example.tubespbp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        setCurrentFragment(FragmentMainMenu())

        val bottom_nav : BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home->setCurrentFragment(FragmentMainMenu())
                R.id.ic_hospital->setCurrentFragment(FragmentRS())
//                R.id.ic_profile->setCurrentFragment(thirdFragment)

            }
            true
        }

    }
    private fun setCurrentFragment(fragment: Fragment){
        if (fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.layout_fragment, fragment)
            transaction.commit()
        }
    }
}