package com.tuwaiq.value.homePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import com.tuwaiq.value.fitnessCalculator.repo.FitnessRepo
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()


    val fitnessRepo = FitnessRepo()


    fun saveUpdate(value: Value){
        valueRepo.updateUserInfo(value)
    }


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