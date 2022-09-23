package com.example.tubespbp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val email: String,
    val tanggallahir: String,
    val noHp: String,
    val password: String,

)