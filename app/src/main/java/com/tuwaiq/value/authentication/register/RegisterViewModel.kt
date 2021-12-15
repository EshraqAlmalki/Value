package com.tuwaiq.value.authentication.register

import androidx.lifecycle.ViewModel
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo

class RegisterViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()

    fun saveUpdate(value: Value){
        valueRepo.updateUserInfo(value)
    }

}