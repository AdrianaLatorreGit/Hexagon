package com.example.hexagontest.data.dataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hexagontest.data.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun salvar(user: UserEntity): Long

    @Query("SELECT * FROM user_table")
   suspend fun getAllUsers(): LiveData<List<UserEntity>>
}
