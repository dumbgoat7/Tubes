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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.api.UserAPI
import com.example.tubespbp.databinding.ActivityRegisterBinding
import com.example.tubespbp.room.User
import com.example.tubespbp.room.UserDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RegisterActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private lateinit var binding:ActivityRegisterBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createNotificationChannel()
        queue = Volley.newRequestQueue(this)
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

                userCheck()

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

    private fun userCheck(){
        val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(binding?.layoutTanggalLahir?.editText?.getText().toString(), formatter)

        val user = com.example.tubespbp.Models.User(
            binding?.layoutUsername?.editText?.getText().toString(),
            binding?.layoutEmail?.editText?.getText().toString(),
            date.toString(),
            binding?.layoutNoHp?.editText?.getText().toString(),
            binding?.layoutPassword?.editText?.getText().toString(),
        )

        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, UserAPI.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var profile = gson.fromJson(response,com.example.tubespbp.Models.User::class.java)
                println(gson)
                if(profile != null)
                    Toast.makeText(this@RegisterActivity, "Register Successfully", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
                AlertDialog.Builder(this@RegisterActivity)
                    .setTitle("Error")
                    .setMessage(error.message)
                    .setPositiveButton("OK", null)
                    .show()
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    println(headers)
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }
//                override fun getParams(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    params["name"] = itemBinding?.ilName?.editText?.getText().toString()
//                    params["notelp"] = itemBinding?.ilNoTelp?.editText?.getText().toString()
//                    params["email"] = itemBinding?.ilEmail?.editText?.getText().toString()
//                    params["birthdate"] = date.toString()
//                    params["password"] = itemBinding?.ilPassword?.editText?.getText().toString()
//                    return params
//                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
}