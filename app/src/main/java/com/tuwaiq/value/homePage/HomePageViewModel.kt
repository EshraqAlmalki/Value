package com.tuwaiq.value.homePage

import androidx.lifecycle.*
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import com.tuwaiq.value.fitnessCalculator.repo.FitnessRepo
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()


    fun getUserInfo(email:String): LiveData<Value?> =
        valueRepo.getUserInfo(email)


    private val fitnessRepo = FitnessRepo()

    private var valueLiveData = MutableLiveData<String>()

    var userInfo:LiveData<Value?> =
        Transformations.switchMap(valueLiveData){
            valueRepo.getUserInfo(it)
        }


    fun getAllUserInfo():LiveData<Value> = valueRepo.getAllUserInfo()




//
//    fun saveAdd(value: Value){
//        valueRepo.addUser(value)
//    }



    fun macrosCount(age:String,gender:String,weight:String,height:String,
                    goal:String,activityLevel:String):LiveData<RapidRespnse>{

        val userInfoLiveData:MutableLiveData<RapidRespnse> = MutableLiveData()
        var rapidRespnse =RapidRespnse()

        viewModelScope.launch{

            rapidRespnse = fitnessRepo.macrosCount(age, gender, weight,
                height, goal, activityLevel)
        }.invokeOnCompletion {
            viewModelScope.launch {
                userInfoLiveData.value = rapidRespnse
            }
        }

        return userInfoLiveData
    }

}