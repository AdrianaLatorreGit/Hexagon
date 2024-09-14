package com.example.hexagontest.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val birth: String,
    val cpf: String,
    val city: String,
    val isActive: Boolean,
    val imageUri: String? = null
)
