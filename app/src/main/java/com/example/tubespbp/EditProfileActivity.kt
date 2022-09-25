package com.example.tubespbp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tubespbp.databinding.ActivityEditProfileBinding
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    var itemBinding: ActivityEditProfileBinding? = null
    var sharedPreferences: SharedPreferences? = null
    private lateinit var editProfileLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemBinding = ActivityEditProfileBinding.inflate(layoutInflater)

        setContentView(itemBinding?.root)

        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")
        itemBinding?.etName?.setText(db?.UserDao()?.getUser(id!!.toInt())?.username)
        itemBinding?.etEmail?.setText(db?.UserDao()?.getUser(id!!.toInt())?.email)
        itemBinding?.etTglLahir?.setText(db?.UserDao()?.getUser(id!!.toInt())?.tanggallahir)
        itemBinding?.etNoHp?.setText(db?.UserDao()?.getUser(id!!.toInt())?.noHp)

        itemBinding?.etTglLahir?.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    itemBinding?.ilTglLahir?.editText?.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

                },
                year,
                month,
                day
            )

            dpd.show()
        }


        itemBinding?.btnSave?.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, LoginActivity::class.java)

            val Name: String = itemBinding?.ilName?.editText?.getText().toString()
            val Email: String = itemBinding?.ilEmail?.editText?.getText().toString()
            val TangalLahir: String = itemBinding?.etTglLahir?.getText().toString()
            val NoTelp: String = itemBinding?.ilNoHp?.editText?.getText().toString()

            var checkSave = true

            if (Name.isEmpty()) {
                itemBinding?.ilName?.setError("Name must be filled with text")
                checkSave = false
            }

            if (NoTelp.isEmpty()) {
                itemBinding?.ilNoHp?.setError("Phone Number must be filled with text")
                checkSave = false
            }

            if (Email.isEmpty()) {
                itemBinding?.ilEmail?.setError("E-mail must be filled with text")
                checkSave = false
            }

            if (!Email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                itemBinding?.ilEmail?.setError("Email tidak valid")
                checkSave = false
            }

            if (TangalLahir.isEmpty()) {
                itemBinding?.etTglLahir?.setError("Birth Date must be filled with text")
                checkSave = false
            }

            if (checkSave == true) {
                setupListener()
                Toast.makeText(
                    applicationContext,
                    "Your Profile Changed",
                    Toast.LENGTH_SHORT
                ).show()
                val moveMenu = Intent(this, HomeActivity::class.java)
                startActivity(moveMenu)
            } else {
                return@OnClickListener
            }
        })
    }


    private fun setupListener() {
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")

        db.UserDao().updateUser(
            User(
                id!!.toInt(),
                itemBinding?.etName?.getText().toString(),
                itemBinding?.etEmail?.text.toString(),
                itemBinding?.etTglLahir?.text.toString(),
                itemBinding?.etNoHp?.text.toString(),
                db?.UserDao()?.getUser(id!!.toInt())?.password.toString()
            )
        )
        finish()
    }

}