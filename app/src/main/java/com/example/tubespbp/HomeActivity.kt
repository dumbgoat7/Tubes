package com.example.tubespbp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        val fragmentProfile = FragmentProfile()
        val fragmentRs = FragmentRS()
        val fragmentMainMenu = FragmentMainMenu()
        val cameraActivity = CameraActivity()

        setCurrentFragment(fragmentMainMenu)

        val bottom_nav : BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottom_nav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.ic_home-> {
                    setCurrentFragment(fragmentMainMenu)
                    true
                }
                R.id.ic_hospital-> {
                    setCurrentFragment(fragmentRs)
                    true
                }
                R.id.ic_scan-> {
                    val intent = Intent(this, CameraActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_profile->{
                    setCurrentFragment(fragmentProfile)
                    true
                }
                else -> false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Disini kita menghubungkan menu yang telah kita buat dengan activity ini
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment){
        if (fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.layout_fragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun setActivity(activity: AppCompatActivity){
        val moveActivity = Intent(this@HomeActivity, EditProfileActivity::class.java)
        startActivity(moveActivity)
    }
}