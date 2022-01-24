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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRun(run: Run)

    @Delete
    fun deleteRun(run: Run)


    @Query("SELECT * FROM `run-model-class` ORDER BY timestamp DESC")
    fun getAllRunSortByDate():LiveData<List<Run>>

    @Query("SELECT SUM(timeInMilli) FROM `run-model-class` ")
    fun getTotalTimeInMillis(): LiveData<Long>

    @Query("SELECT SUM(distanceInMeters) FROM `run-model-class` ")
    fun getTotalDistance(): LiveData<Int>


    @Query("SELECT * FROM `run-model-class`")
    fun getAllActivities():LiveData<List<Run>>


    @Query("SELECT SUM(avgSpeedInKMH) FROM `run-model-class` ")
    fun getTotalAvgSpeed(): LiveData<Float>


    @Query("SELECT SUM(caloriesBurned) FROM `run-model-class` ")
    fun getTotalCaloriesBurned(): LiveData<Int>


}