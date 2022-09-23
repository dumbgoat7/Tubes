package com.example.tubespbp.room

import androidx.room.*

@Dao
interface NewsDao {
    @Insert
    fun addNews(news: News)

    @Update
    fun updateNews(news: News)

    @Delete
    fun deleteNews(news: News)

    @Query("SELECT * FROM news")
    fun getAllNews() : List<News>

    @Query("SELECT * FROM news WHERE id =:news_id")
    fun getNews(news_id: Int) : List<News>
}