package com.example.tubespbp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tubespbp.databinding.ActivityRegisterBinding
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import com.example.tubespbp.room.UserDao
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.google.android.material.datepicker.MaterialDatePicker
import javax.xml.datatype.DatatypeConstants.MONTHS

class RegisterActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private lateinit var binding:ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setTitle("Daftar")

        binding.layoutTanggalLahir.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    binding.etTglLahir.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)
                }, year, month, day)
            datePicker.show()
        }

        binding.btnRegister.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            val username: String = binding.layoutUsername.editText?.getText().toString()
            val email: String = binding.layoutEmail.editText?.getText().toString()
            val tanggalLahir: String = binding.layoutTanggalLahir.editText?.getText().toString()
            val noHp: String = binding.layoutNoHp.editText?.getText().toString()
            val password: String = binding.layoutPassword.editText?.getText().toString()

            var checkRegister = true

            if (username.isEmpty()){
                binding.layoutUsername.setError("Username must be filled with text")
                checkRegister = false
            }

            if (email.isEmpty()){
                binding.layoutEmail.setError("Email must be filled with text")
                checkRegister = false
            }

            if (!email.matches(Regex("[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                binding.etEmail.setError("Email Invalid")
                checkRegister = false
            }

            if (tanggalLahir.isEmpty()){
                binding.layoutTanggalLahir.setError("Tanggal lahir must be filled with text")
                checkRegister = false
            }

            if (noHp.isEmpty()){
                binding.layoutNoHp.setError("Nomor Hp must be filled with text")
                checkRegister = false
            }

            if (noHp.length != 12) {
                binding.etNoHp.setError("Phone number length must be 12 digit")
                checkRegister = false
            }

            if (password.isEmpty()){
                binding.layoutPassword.setError("Password must be filled with text")
                checkRegister = false
            }

            if(checkRegister == true) {
                val db by lazy { UserDB(this)}
                val userDao = db.UserDao()

                val user = User(0, username, email, tanggalLahir, noHp, password)
                userDao.addUser(user)

                val moveLogin = Intent(this, LoginActivity::class.java)
                val bundle: Bundle = Bundle()
                bundle.putString("username", username)
                bundle.putString("email", email)
                bundle.putString("tanggalLahir", tanggalLahir)
                bundle.putString("nohp", noHp)
                bundle.putString("password", password)
                moveLogin.putExtra("register", bundle)
                startActivity(moveLogin)

            } else {
                return@OnClickListener
            }
        })
        binding.btnRegister.setOnClickListener(View.OnClickListener {
            val moveLogin = Intent(this, LoginActivity::class.java)
            startActivity(moveLogin)
        })
    }
}