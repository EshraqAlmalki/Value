package com.tuwaiq.value.database

import android.content.ContentValues.TAG
import android.content.Context
import android.icu.text.CaseMap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException
import java.util.concurrent.Executors
import kotlin.math.E

const val DATABASE_NAME = "my-database"
class ValueRepo private constructor(context: Context){


    private val database:ValueDatabase= Room.databaseBuilder(context
        .applicationContext,
        ValueDatabase::class.java,
        DATABASE_NAME)
        .build()


    private val userPhysicalInfo = Firebase.firestore
        .collection("user-physical-info")

    private val document = Firebase.firestore
        .collection("user-physical-info").document()



    fun retrieverUserInfo(email: String):LiveData<Value> = liveData {
        val getUserPhysicalInfo = Firebase.firestore

      val dataList =  getUserPhysicalInfo.collection("user-physical-info")
            .whereEqualTo("email",email)
            .get()
            .await().toObjects(Value::class.java)
        emit(dataList[0])
    }


    fun retrieverUserActivity(steps : String):LiveData<Value> = liveData {
        val getUserActivity = Firebase.firestore

        val userActive = getUserActivity.collection("user-physical-info")
            .whereEqualTo("steps", steps)
            .get().await().toObjects(Value::class.java)
        emit(userActive[0])
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

    fun updateFirestore(value: Value) = CoroutineScope(Dispatchers.IO).launch {

        value.documentId = document.id

        try {

            userPhysicalInfo.document(value.documentId)
                .update("weight",value.weight ,
                "height" , value.height ,
                    "weightGoal" , value.weightGoal , "age" ,
                value.age , "gender" , value.gender , "active"
                , value.active).await()
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

    fun getUserSteps(steps:Int):List<Value>{
        return valueDao.getUserSteps(steps)

    }

    fun getStepsGoal(stGoal:Int):List<Value>{
        return valueDao.getStepsGoal(stGoal)
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