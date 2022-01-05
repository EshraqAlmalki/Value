package com.tuwaiq.value.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ValueDao {

    @Query("SELECT * FROM value")
    fun getAllUserInfo():LiveData<List<Value>>

    @Query("SELECT * FROM value WHERE email=(:email)")
    fun getUserInfo(email :String):LiveData<Value?>

    @Update
    fun updateUserInfo(value: Value)

    @Delete
    fun deleteUserInfo(value: Value)

    @Insert
    fun addNewUser(value: Value)

    @Query("SELECT * FROM value WHERE steps=(:steps)")
    fun getUserSteps(steps :Int):List<Value>

    @Query("SELECT * FROM value WHERE stGoal=(:stGoal)")
    fun getStepsGoal(stGoal : Int):List<Value>
}