package com.tuwaiq.value.database

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData

import androidx.room.Room

import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlinx.coroutines.tasks.await

import java.lang.IllegalStateException

import java.util.concurrent.Executors

const val DATABASE_NAME = "my-database"
class ValueRepo private constructor(context: Context){


    private val database:ValueDatabase= Room.databaseBuilder(context
        .applicationContext,
        ValueDatabase::class.java,
        DATABASE_NAME)
        .build()


    private val userPhysicalInfo = Firebase.firestore
        .collection("user-physical-info")


    fun retrieverUserInfo():LiveData<Value> = liveData {
        val getUserInfo = Firebase.firestore
        val userInfo = Firebase.auth.currentUser?.let {
            getUserInfo.collection("user-physical-info")
                .document(it.uid).get().await().toObject(Value::class.java)

        }

        Log.e(TAG, "retrieverUserInfo: $userInfo" )

            emit(userInfo!!)



    }




    fun saveFireStore(value:Value){

            val addUserInfo = Firebase.auth.currentUser?.let { userPhysicalInfo.document(it.uid) }
        if (addUserInfo != null) {
            value.documentId = addUserInfo.id
        }

        addUserInfo?.set(value)
    }



    fun updateFirestore(value: Value) {
        val dataMap = mapOf(
            "gender" to value.gender,
            "active" to value.active,
            "age" to value.age,
            "weight" to value.weight,
            "height" to value.height,
            "stGoal" to value.stGoal,
            "weightGoal" to value.weightGoal
        )

        Firebase.auth.currentUser?.let {
            Firebase.firestore
                .collection("user-physical-info")
                .document(it.uid).update(dataMap)
            Log.e(TAG, "updateFirestore: ${it.uid}" )
        }
        Log.e(TAG, "updateFirestore: $dataMap" )
        

    }


    fun deleteFirestore(value: Value) {
        val dataMap = mapOf(
            "gender" to value.gender,
            "active" to value.active,
            "age" to value.age,
            "weight" to value.weight,
            "height" to value.height,
            "stGoal" to value.stGoal,
            "weightGoal" to value.weightGoal
        )

        Firebase.auth.currentUser?.let {
            Firebase.firestore
                .collection("user-physical-info")
                .document(it.uid).delete()
            Log.e(TAG, "updateFirestore: ${it.uid}" )
        }
        Log.e(TAG, "updateFirestore: $dataMap" )


    }

    fun deleteAccount(value: Value){
        val user = Firebase.auth.currentUser!!
        user.delete().addOnCompleteListener {
            if (it.isSuccessful){
                Log.e(TAG, "deleteFirestore: deleted" )
            }
        }
    }


    private val valueDao = database.valueDao()

    private val executor = Executors.newSingleThreadExecutor()


    fun getAllUserInfo(): LiveData<List<Value>> = valueDao.getAllUserInfo()

    fun getUserInfo(email:String) : LiveData<Value?> {
            return valueDao.getUserInfo(email)

    }

    fun updateUserInfo(value: Value){
        executor.execute {
            valueDao.updateUserInfo(value)
        }
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

    fun addRun(run: Run){
        executor.execute {
            valueDao.insertRun(run)
        }
    }

    fun getAllActivities():LiveData<List<Run>> = valueDao.getAllActivities()
    fun deleteRun(run: Run){
        executor.execute {
            valueDao.deleteRun(run)
        }
    }

    fun getAllRunSortByDate():LiveData<List<Run>>{

            return valueDao.getAllRunSortByDate()

    }



    companion object{
        private var INSTANCE : ValueRepo?= null
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