package com.example.tubespbp.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addNote(user: User)

    @Update
    suspend fun updateNote(user: User)

    @Delete
    suspend fun deleteNote(user: User)

    @Query("SELECT * FROM user")
    suspend fun getNotes() : List<User>

    @Query("SELECT * FROM user WHERE id =:note_id")
    suspend fun getNote(note_id: Int) : List<User>

}