package com.tuwaiq.value.database

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.Query
import androidx.room.Room
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
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

//    fun retrieverUserInfo(email: String):LiveData<Value> = liveData {
//            val getUserPhysicalInfo = Firebase.firestore
//
//            val dataList = getUserPhysicalInfo.collection("user-physical-info")
//                .whereEqualTo("email", email)
//                .get()
//                .await().toObjects(Value::class.java)
//            emit(dataList[0])
//    }


    fun retrieverUserInfo(email: String):LiveData<Value> = liveData{
        val getUserPhysicalInfo = Firebase.firestore


        getUserPhysicalInfo.collection("user-physical-info")
            .whereEqualTo("email",email).addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "onEvent: ${error.message.toString()}",)
                    return@addSnapshotListener
                }


                for (doc in value!!.documentChanges) {
                    when(doc.type){
                        DocumentChange.Type.MODIFIED ->
                            return@addSnapshotListener
                    }


                }

            }
    }




//    fun retrieverUserActivity(steps : String):LiveData<Value> = liveData {
//        val getUserActivity = Firebase.firestore
//
//        val userActive = getUserActivity.collection("user-physical-info")
//            .whereEqualTo("steps", steps)
//            .get().await().toObjects(Value::class.java)
//        emit(userActive[0])
//    }



    fun saveFireStore(value:Value){

            val addUserInfo = userPhysicalInfo.document()
            value.documentId = addUserInfo.id



            addUserInfo.set(value)
    }



    fun updateFirestore(id:String,value: Value) {

//        val getRef:FirebaseFirestore =FirebaseFirestore.getInstance()
//        val getDocumentRef : CollectionReference= getRef.collection("user-physical-info")
//        val docId:com.google.firebase.firestore.Query = getDocumentRef.whereEqualTo("documentId",value.documentId)
//        docId.get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
//            override fun onComplete(p0: Task<QuerySnapshot?>) {
//                if (p0.isSuccessful){
//                    for (document in p0.result!!){
//                        document.getDocumentReference(value.documentId)?.update("weight",value.weight)
//                            ?.addOnSuccessListener(object : OnSuccessListener<Void?> {
//                                override fun onSuccess(p0: Void?) {
//                                    Log.e(TAG, "onSuccess: document successfully updated", )
//
//                                }
//
//                            })?.addOnFailureListener(object : OnFailureListener {
//                                override fun onFailure(p0: Exception) {
//                                    Log.e(TAG, "onFailure: ",p0 )
//                                }
//
//                            })
//                    }
//
//                }else{
//                    Log.e(TAG, "error getting document ",p0.exception )
//                }
//            }
//
//        })
//
        var idd = ""
        userPhysicalInfo.get().addOnSuccessListener {
            it.forEach {
                idd = it.getString("documentId").toString()
            }
        }.addOnCompleteListener {
            Log.e(TAG, "updateFirestore: ${idd}",)

            val updateUserInfo = userPhysicalInfo.document(idd)

            try {

                updateUserInfo.update("active", value.active)
            } catch (E: Exception) {

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