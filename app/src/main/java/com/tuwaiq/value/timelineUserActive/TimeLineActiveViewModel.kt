package com.tuwaiq.value.timelineUserActive

import androidx.lifecycle.*
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo

class TimeLineActiveViewModel : ViewModel() {


    private val valueRepo = ValueRepo.get()

    private var valueLiveData = MutableLiveData<String>()


    val userStepsLiveData = valueRepo.getAllUserInfo()

    var userInfo: LiveData<List<Value>> =
        Transformations.switchMap(valueLiveData){
            valueRepo.getAllUserInfo()
        }

    var tests = mutableListOf<Value>()

    init {


        for ( o in 1..3){
            val value = Value()
            value.stGoal
            tests+=value
        }


    }


    fun getAllUserInfo(): LiveData<List<Value>> = valueRepo.getAllUserInfo()


    fun retrieverUserActivity(steps: String) :LiveData<Value> = valueRepo.retrieverUserActivity(steps)




    fun deleteUserInfo(value: Value){

            valueRepo.deleteUserInfo(value)

    }

}