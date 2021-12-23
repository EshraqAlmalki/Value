package com.tuwaiq.value.authentication.register

import androidx.lifecycle.*
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import com.tuwaiq.value.fitnessCalculator.models.Data
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import com.tuwaiq.value.fitnessCalculator.repo.FitnessRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NextRegisterViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()
    //private val userInfo : MutableLiveData<String> = MutableLiveData()

//    fun saveUpdate(value: Value){
//        valueRepo.updateUserInfo(value)
//    }

    fun addNewUser(value: Value){
        valueRepo.addNewUser(value)
    }


//    init {
//        userInfo.value = ""
//    }

    private val fitnessRepo = FitnessRepo()

    fun macrosCount(age:String,gender:String,weight:String,height:String,
                    goal:String,activityLevel:String):LiveData<RapidRespnse>{

        val userInfoLiveData:MutableLiveData<RapidRespnse> = MutableLiveData()
            var rapidRespnse =RapidRespnse()


            viewModelScope.launch{

                rapidRespnse = fitnessRepo.macrosCount(age, gender, weight,
                    height, goal , activityLevel)

            }.invokeOnCompletion {
                viewModelScope.launch {
                    userInfoLiveData.value = rapidRespnse
                }
            }

           return userInfoLiveData
        }

    }



