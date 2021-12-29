package com.tuwaiq.value.authentication.logIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import kotlinx.coroutines.flow.Flow

class LoginViewModel : ViewModel() {



    private val valueRepo = ValueRepo.get()

    fun getUserInfo(email:String):LiveData<Value?> {
        return valueRepo.getUserInfo(email)

    }

//    fun getMacrosInfo(value:Value){
//        valueRepo.getMacrosInfo(value)
//    }

//    fun saveFirestore(value: Value){
//        valueRepo.saveFireStore(value)
//    }




}