package com.example.tubespbp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespbp.Models.News
import kotlinx.android.synthetic.main.rv_item_news.view.*

class NewsAdapter (private val news: ArrayList<News>,
private val listener :OnAdapterListener) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_news,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news1 = news[position]
        holder.view.tv_judul_news.text = news1.judul
        holder.view.tv_deskripsi.text = news1.deskripsi
        holder.view.tv_judul_news.setOnClickListener{
            listener.onClick(news1)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(news1)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(news1)
        }
    }

    override fun getItemCount(): Int {
        return news.size
    }

    inner class NewsViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<News>) {
        news.clear()
        news.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(note:News)
        fun onUpdate(note:News)
        fun onDelete(note:News)
    }

}