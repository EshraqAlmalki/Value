package com.tuwaiq.value.homePage

import androidx.lifecycle.*
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import com.tuwaiq.value.fitnessCalculator.repo.FitnessRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()
    private var valueLiveData = MutableLiveData<String>()

    var userInfo:LiveData<Value?> =
        Transformations.switchMap(valueLiveData){
            valueRepo.getUserInfo(it)
        }



    fun retrieverUserInfo(uid: String):LiveData<Value> = valueRepo.retrieverUserInfo()

    fun getUserInfo(email:String){
       valueLiveData.value = email
    }

    fun updateFirestore(value: Value){
        valueRepo.updateFirestore(value )
    }



    fun updateUserInfo(value: Value){
        valueRepo.updateUserInfo(value)
    }



    private val fitnessRepo = FitnessRepo()

    fun macrosCount(age:String,gender:String,weight:String,height:String,
                    goal:String,activityLevel:String):LiveData<RapidRespnse>{

        val userInfoLiveData:MutableLiveData<RapidRespnse> = MutableLiveData()
        var rapidRespnse = RapidRespnse()


        viewModelScope.launch(Dispatchers.Main){


            rapidRespnse = fitnessRepo.macrosCount(age, gender, weight,
                height, goal , activityLevel)



        }.invokeOnCompletion {

                userInfoLiveData.value = rapidRespnse

        }

        return userInfoLiveData
    }


}