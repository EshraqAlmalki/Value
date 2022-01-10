package com.tuwaiq.value.homePage

import androidx.lifecycle.*
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import com.tuwaiq.value.fitnessCalculator.repo.FitnessRepo
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


}