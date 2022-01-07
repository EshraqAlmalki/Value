package com.tuwaiq.value.firestoreUserInfo.personalInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo

class PersonalInfoViewModel : ViewModel() {
    private val valueRepo = ValueRepo.get()

    private var valueLiveData = MutableLiveData<String>()

    var userInfo:LiveData<Value?> =
        Transformations.switchMap(valueLiveData){
            valueRepo.getUserInfo(it)
        }

    fun getUserInfo(email:String): LiveData<Value?> {
        return valueRepo.getUserInfo(email)
    }

    fun updateFirestore(id:String,value: Value){
       valueRepo.updateFirestore(id,value )
    }

    fun updateUserInfo(value: Value){
        valueRepo.updateUserInfo(value)
    }




    fun retrieverUserInfo(email: String):LiveData<Value> =
        valueRepo.retrieverUserInfo(email)

}