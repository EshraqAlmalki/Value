package com.tuwaiq.value.authentication.register

import androidx.lifecycle.*
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import com.tuwaiq.value.fitnessCalculator.repo.FitnessRepo
import kotlinx.coroutines.launch

class NextRegisterViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()


    fun saveUpdate(value: Value){
        valueRepo.updateUserInfo(value)
    }



    val fitnessRepo = FitnessRepo()

    fun macrosCount(age:String, gender:String, weight:String,
                            height:String, goal:String,
                            activityLevel:String) {
        viewModelScope.launch {
            var liveData = fitnessRepo
                .macrosCount(
                    age,
                    gender, weight,
                    height, goal,
                    activityLevel
                )
        }

    }

}