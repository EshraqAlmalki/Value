package com.tuwaiq.value.homePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import com.tuwaiq.value.fitnessCalculator.models.MacrosCountResponse
import com.tuwaiq.value.fitnessCalculator.repo.FitnessRepo

class HomePageViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()


    val fitnessRepo = FitnessRepo()


    fun saveUpdate(value: Value){
        valueRepo.updateUserInfo(value)
    }


    fun macrosCount(age:String, gender:String,weight:String,
                    height:String,goal:String,
                    activityLevel:String): LiveData<MacrosCountResponse> = fitnessRepo
        .macrosCount(age,
            gender,weight,
            height,goal,
            activityLevel)
}