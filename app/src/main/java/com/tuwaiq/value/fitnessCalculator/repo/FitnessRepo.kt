package com.tuwaiq.value.fitnessCalculator.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tuwaiq.value.fitnessCalculator.api.FitnessCalculatorApi
import com.tuwaiq.value.fitnessCalculator.models.RapidRespnse
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FitnessRepo"
class FitnessRepo {





    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://fitness-calculator.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(FitnessCalculatorApi::class.java)


   suspend fun macrosCount(age:String, gender:String, weight:String,
                           height:String, goal:String,
                           activityLevel:String):RapidRespnse{


       val response = api.calculatorMacros(age, gender, weight, height,
           activityLevel, goal).awaitResponse()
       var rapidRespnse = RapidRespnse()

       if (response.isSuccessful){
           rapidRespnse =response.body()!!
       }else{
           Log.e(TAG, "the error is: ${response.raw()}")
       }

       return rapidRespnse

   }
}