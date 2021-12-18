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

    fun saveUpdate(value: Value){
        valueRepo.updateUserInfo(value)
    }


//    init {
//        userInfo.value = ""
//    }

    private val fitnessRepo = FitnessRepo()

    fun macrosCount(age:String,gender:String,weight:String,height:String,
                    goal:String,activityLevel:String):LiveData<RapidRespnse>{

        var userInfoLiveData:MutableLiveData<RapidRespnse> = MutableLiveData()


            viewModelScope.launch(Dispatchers.IO) {

                userInfoLiveData = fitnessRepo.macrosCount(age, gender, weight,
                    height, goal, activityLevel) as MutableLiveData<RapidRespnse>
            }

           return userInfoLiveData
        }


    }

//    fun sendQuery(query : String){
//        userInfo.value = query
//    }

