package com.tuwaiq.value.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.concurrent.Executors

const val DATABASE_NAME = "my-database"
class ValueRepo private constructor(context: Context){


    private val database:ValueDatabase= Room.databaseBuilder(context
        .applicationContext, ValueDatabase::class.java,DATABASE_NAME).build()




    private val valueDao = database.valueDao()

    private val executor = Executors.newSingleThreadExecutor()


    fun getAllUserInfo(): LiveData<Value> = valueDao.getAllUserInfo()


    fun updateUserInfo(value: Value){
        executor.execute {
            valueDao.updateUserInfo(value)
        }
    }

    fun deleteUserInfo(value: Value){
        executor.execute {
            valueDao.deleteUserInfo(value)
        }
    }



    companion object{
        var INSTANCE : ValueRepo?= null
        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = ValueRepo(context)
            }
        }

        fun get():ValueRepo{
            return INSTANCE?: throw IllegalStateException(" ValueRepo must be initialized")
        }

    }
}