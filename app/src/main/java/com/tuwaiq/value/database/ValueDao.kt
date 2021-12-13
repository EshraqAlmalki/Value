package com.tuwaiq.value.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update


@Dao
interface ValueDao {

    @Query("SELECT * FROM value")
    fun getAllUserInfo():LiveData<Value>

    @Update
    fun updateUserInfo(value: Value)

    @Delete
    fun deleteUserInfo(value: Value)







}