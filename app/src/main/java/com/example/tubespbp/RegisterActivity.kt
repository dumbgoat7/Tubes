package com.example.tubespbp

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tubespbp.databinding.ActivityRegisterBinding
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RegisterActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private lateinit var binding:ActivityRegisterBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createNotificationChannel()
        setTitle("Daftar")

        binding.etTglLahir.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    binding.etTglLahir.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)
                }, year, month, day)
            datePicker.show()
        }

        binding.btnRegister.setOnClickListener(View.OnClickListener{

            val username: String = binding.layoutUsername.editText?.getText().toString()
            val email: String = binding.layoutEmail.editText?.getText().toString()
            val tanggalLahir: String = binding.etTglLahir.getText().toString()
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
                binding.layoutEmail.setError("Email Invalid")
                checkRegister = false
            }

            if (tanggalLahir.isEmpty()){
                binding.etTglLahir.setError("Tanggal lahir must be filled with text")
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
            if (password.length !=8){
                binding.etPass.setError("Password should more than contain 8 character")
                checkRegister = false
            }

            if(checkRegister == true) {

                val user = User(0, username, email, tanggalLahir, noHp, password)
                CoroutineScope(Dispatchers.IO).launch{
                    db.UserDao().addUser(user)
                    finish()
                }
                sendNotification()
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
    }
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification() {
        val intent : Intent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent = PendingIntent.getActivity(this,0, intent, 0)
        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Account "+binding.layoutUsername.editText?.getText().toString()+" Registered")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Nama Aplikasi")
            .setContentText("Success Registering Account "+binding.layoutUsername.editText?.getText().toString())
            .setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.welcome))
                )
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.CYAN)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "check", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1, builder.build())
        }
    }
}