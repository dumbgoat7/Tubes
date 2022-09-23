package com.example.tubespbp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class News (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val judul: String,
    val deskripsi: String,
)

