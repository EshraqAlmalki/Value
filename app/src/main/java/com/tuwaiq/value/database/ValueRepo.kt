package com.tuwaiq.value.database

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.util.concurrent.Executors

const val DATABASE_NAME = "my-database"
class ValueRepo private constructor(context: Context){


    private val database:ValueDatabase= Room.databaseBuilder(context
        .applicationContext,
        ValueDatabase::class.java,
        DATABASE_NAME)
        .build()


    private val userPhysicalInfo = Firebase.firestore.collection("user-physical-info")

     fun retrieverUserInfo() = CoroutineScope(Dispatchers.IO).launch {
        try {

            val querySnapshot = userPhysicalInfo.get().await()
            val sb = StringBuilder()
            for (document in querySnapshot.documents) {
                val value = document.toObject<Value>()
                sb.append("$value\n")
            }

            withContext(Dispatchers.Main){
                Log.e(TAG, "retrieverUserInfo: good job", )
            }

        } catch (E: Exception) {


            withContext(Dispatchers.Main) {
                Log.e(TAG, "retrieverUserInfo: hi from here",)
            }
        }
    }

    fun saveFireStore(value:Value) = CoroutineScope(Dispatchers.IO).launch { 
        try{
            userPhysicalInfo.add(value).await()
            Log.d(TAG, "saveFireStore: good")
        }catch (E:Exception){
            withContext(Dispatchers.Main){
                Log.d(TAG, E.message.toString())
            }
        }
    }


    private val valueDao = database.valueDao()

    private val executor = Executors.newSingleThreadExecutor()


    fun getAllUserInfo(): LiveData<List<Value>> = valueDao.getAllUserInfo()

    fun getUserInfo(email:String): LiveData<Value?> {
            return valueDao.getUserInfo(email)

    }

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

    fun addNewUser(value: Value){
        executor.execute {
            valueDao.addNewUser(value)
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
            return INSTANCE?:throw IllegalStateException(" ValueRepo must be initialized")
        }
    }
}