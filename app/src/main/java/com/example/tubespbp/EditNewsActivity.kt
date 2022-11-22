package com.example.tubespbp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.api.NewsAPI
import com.example.tubespbp.room.Constant
import com.example.tubespbp.Models.News
import com.example.tubespbp.room.UserDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_news.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditNewsActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private var newsId : Int = 0
    private val CHANNEL_ID = "channel_notification"
    private val notificationId = 100
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_news)
        queue = Volley.newRequestQueue(this)
        createNotificationChannel()
        setupView()
        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type",0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                edit_title.keyListener = null
                edit_note.keyListener = null
                getNewsByID()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getNewsByID()
            }
        }
    }

    private fun setupListener() {
        button_save.setOnClickListener {
            createNews()
//            CoroutineScope(Dispatchers.IO).launch {
//                db.NewsDao().addNews(
//                    News(0,edit_title.text.toString(),
//                        edit_note.text.toString())
//                )
//                sendNotificationSave()
//                finish()
//            }
        }

        button_update.setOnClickListener {
            updateNews()
//            CoroutineScope(Dispatchers.IO).launch {
//                db.NewsDao().updateNews(
//                    News(newsId,edit_title.text.toString(),
//                        edit_note.text.toString())
//                )
//                sendNotificationUpdate()
//                finish()
//            }
        }
    }

    fun getNewsByID() {
        newsId = intent.getIntExtra("intent_id",0)

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, NewsAPI.GET_BY_ID_URL + newsId,
                { response ->
                    val news = Gson().fromJson(response, News::class.java)
                    edit_title!!.setText(news.judul)
                    edit_note!!.setText(news.deskripsi)
                    Toast.makeText(
                        this@EditNewsActivity,
                        "Data berhasil diambil",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditNewsActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
//        CoroutineScope(Dispatchers.IO).launch {
//            val notes = db.NewsDao().getNews(newsId)[0]
//            edit_title.setText(notes.judul)
//            edit_note.setText(notes.deskripsi)
//        }
    }

    private fun createNews() {
        val news = News(
            edit_title!!.text.toString(),
            edit_note!!.text.toString()
        )
        val stringRequest: StringRequest =
            object :
                StringRequest(Method.POST, NewsAPI.ADD_URL, Response.Listener { response ->
                    val gson = Gson()
                    var mahasiswa = gson.fromJson(response, News::class.java)

                    if (mahasiswa != null)
                        sendNotificationSave()
                        finish()

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }, Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditNewsActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(news)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        queue!!.add(stringRequest)
    }

    private fun updateNews() {
        newsId = intent.getIntExtra("intent_id",0)
        val news = News(
            edit_title!!.text.toString(),
            edit_note!!.text.toString()
        )
        val stringRequest: StringRequest =
            object :
                StringRequest(Method.PUT, NewsAPI.UPDATE_URL + newsId, Response.Listener { response ->
                    val gson = Gson()
                    var mahasiswa = gson.fromJson(response, News::class.java)

                    if (mahasiswa != null)
                        sendNotificationUpdate()
                        finish()

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }, Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditNewsActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(news)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        queue!!.add(stringRequest)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotificationSave(){
        val bigtext = edit_note.getText().toString()
        val title = edit_title.getText().toString()
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Breaking News!")
            .setColor(Color.GREEN)
//            .setGroup()
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(bigtext)
                    .setBigContentTitle(title)
                    .setSummaryText("Hot News!"))
        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }

    }
    private fun sendNotificationUpdate(){
        val title = edit_title.getText().toString()
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Latest Update!")
            .setColor(Color.GREEN)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setBigContentTitle("Latest Update!")
                    .setSummaryText("Update News!")
                    .addLine(title)
                    .addLine("This is the latest update"))
        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }

    }

}