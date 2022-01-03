package com.tuwaiq.value.steps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo

class StepsCountViewModel : ViewModel() {

    val valueRepo = ValueRepo.get()


    private var valueLiveData = MutableLiveData<String>()

    var userInfo: LiveData<Value?> =
        Transformations.switchMap(valueLiveData){
            valueRepo.getUserInfo(it)
        }

    fun getUserInfo(email:String){
        valueLiveData.value = email
    }


    fun updateUserInfo(value: Value){

            valueRepo.updateUserInfo(value)

    }
}