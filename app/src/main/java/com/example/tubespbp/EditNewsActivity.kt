package com.example.tubespbp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tubespbp.room.Constant
import com.example.tubespbp.room.News
import com.example.tubespbp.room.UserDB
import kotlinx.android.synthetic.main.activity_edit_news.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNewsActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private var newsId : Int = 0
    private val CHANNEL_ID = "channel_notification"
    private val notificationId = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_news)
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
                getNews()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getNews()
            }
        }
    }

    private fun setupListener() {
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.NewsDao().addNews(
                    News(0,edit_title.text.toString(),
                        edit_note.text.toString())
                )
                sendNotificationSave()
                finish()
            }
        }

        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.NewsDao().updateNews(
                    News(newsId,edit_title.text.toString(),
                        edit_note.text.toString())
                )
                sendNotificationSave()
                finish()
            }
        }
    }

    fun getNews() {
        newsId = intent.getIntExtra("intent_id",0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.NewsDao().getNews(newsId)[0]
            edit_title.setText(notes.judul)
            edit_note.setText(notes.deskripsi)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun sendNotificationSave(){
        val bigtext = edit_note.getText().toString()
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Hot News!!")
            .setContentText(edit_title.getText().toString() + "\n" + edit_note.getText().toString())
            .setColor(Color.GREEN)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(bigtext))
        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }

    }
}