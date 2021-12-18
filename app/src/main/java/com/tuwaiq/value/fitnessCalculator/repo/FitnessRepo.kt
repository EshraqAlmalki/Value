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
                           activityLevel:String):LiveData<RapidRespnse>{

       val response = api.calculatorMacros(age, gender, weight, height,
           activityLevel, goal).awaitResponse()
       val member:MutableLiveData<RapidRespnse> = MutableLiveData()

       if (response.isSuccessful){
           Log.e(TAG, "macrosCount: wrong")
           member.value =response.body()!!
       }else{
           Log.e(TAG, "the error is: ${response.errorBody()}")
       }
//       api.calculatorMacros(age, gender, weight, height,
//           activityLevel, goal).await()

//       api.calculatorMacros(age, gender, weight, height,
//           activityLevel, goal).await().data.balanced

//       api.calculatorMacros(age, gender, weight, height,
//           activityLevel, goal).enqueue( object :Callback<RapidRespnse>{
//
//
//           override fun onResponse(call: Call<RapidRespnse>, response: Response<RapidRespnse>) {
//               if (response.isSuccessful){
//                   member.value =response.body()!!
//               }else{
//                   Log.d(TAG , "the response is not successful")
//               }
//           }
//
//           override fun onFailure(call: Call<RapidRespnse>, t: Throwable) {
//               Log.e(TAG, "there is no response")
//           }
//       })

       return member

   }

}