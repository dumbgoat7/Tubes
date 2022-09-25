package com.example.tubespbp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
                finish()
            }
        }

        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.NewsDao().updateNews(
                    News(newsId,edit_title.text.toString(),
                        edit_note.text.toString())
                )
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
}