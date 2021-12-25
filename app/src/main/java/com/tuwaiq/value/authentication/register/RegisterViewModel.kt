package com.tuwaiq.value.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo

class RegisterViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()

    fun saveUpdate(value: Value){
        valueRepo.updateUserInfo(value)
    }


    fun addNewUser(value: Value){
        valueRepo.addNewUser(value)
    }

    private var userLiveData = MutableLiveData<String>()


    var userInfo: LiveData<Value?> =
        Transformations.switchMap(userLiveData){
            valueRepo.getUserInfo(it)
        }

}