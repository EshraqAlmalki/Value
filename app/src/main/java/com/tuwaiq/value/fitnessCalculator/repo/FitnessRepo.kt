package com.tuwaiq.value.fitnessCalculator.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tuwaiq.value.fitnessCalculator.api.FitnessCalculatorApi
import com.tuwaiq.value.fitnessCalculator.models.MacrosCountResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FitnessRepo"
class FitnessRepo {

    var member:MutableLiveData<MacrosCountResponse> = MutableLiveData()

    private val client: OkHttpClient = OkHttpClient.Builder()
        .build()



    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://fitness-calculator.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val api = retrofit.create(FitnessCalculatorApi::class.java)


   fun macrosCount(age:String, gender:String,weight:String,
                   height:String,goal:String,
                   activityLevel:String):LiveData<MacrosCountResponse>{

       api.calculatorMacros(age, gender, weight, height,
           activityLevel, goal).enqueue( object :Callback<MacrosCountResponse>{
           override fun onResponse(call: Call<MacrosCountResponse>, response: Response<MacrosCountResponse>) {

               if (response.isSuccessful){
                   member.value =response.body()!!
               }else{
                   Log.d(TAG , "the response is not successful")
               }
           }

           override fun onFailure(call: Call<MacrosCountResponse>, t: Throwable) {
             Log.e(TAG, "there is no response")
           }

       })
       return member
   }

}