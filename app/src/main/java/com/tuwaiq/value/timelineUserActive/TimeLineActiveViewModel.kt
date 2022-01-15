package com.tuwaiq.value.timelineUserActive

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.tuwaiq.value.database.Value
import com.tuwaiq.value.database.ValueRepo
import com.tuwaiq.value.newsOfHealthApi.models.HealthNews
import com.tuwaiq.value.newsOfHealthApi.models.HealthNewsItem
import com.tuwaiq.value.newsOfHealthApi.models.News
import com.tuwaiq.value.newsOfHealthApi.repo.HealthNewsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimeLineActiveViewModel : ViewModel() {


    private val valueRepo = ValueRepo.get()
    private val healthNewsRepo = HealthNewsRepo()

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

    fun getNews(): LiveData<List<HealthNewsItem>> {


        var healthNewsItem:List<HealthNewsItem> = emptyList()
        val newsLiveData:MutableLiveData<List<HealthNewsItem>> = MutableLiveData()

        viewModelScope.launch{
            healthNewsRepo.getAllNews()
        }.invokeOnCompletion {
            viewModelScope.launch {
                newsLiveData.value = healthNewsItem
            }
        }
        Log.e(TAG, "getNews:${newsLiveData.value} ", )


        return newsLiveData
    }



    fun getUserInfo(email:String): LiveData<Value?> {
        return valueRepo.getUserInfo(email)
    }


    fun addNewUser(value: Value){
        valueRepo.addNewUser(value)
    }


    fun getAllUserInfo(): LiveData<List<Value>> = valueRepo.getAllUserInfo()


   // fun retrieverUserActivity(steps: String) :LiveData<Value> = valueRepo.retrieverUserActivity(steps)




    fun deleteUserInfo(value: Value){

            valueRepo.deleteUserInfo(value)

    }

    fun getUserGoal(stGoal:Int):List<Value> = valueRepo.getStepsGoal(stGoal)

}