package com.tuwaiq.value.fitnessCalculator.api

import com.tuwaiq.value.fitnessCalculator.models.MacrosCount
import com.tuwaiq.value.fitnessCalculator.models.MacrosCountResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FitnessCalculatorApi {



    @GET("macrocalculator?rapidapi-key=496a49e199mshd213b6628eb11f1p12dc17jsne3e8d98c2aaf")
    fun calculatorMacros(@Query("age") age:String,
                         @Query("gender") gender:String,
                         @Query("weight") weight:String,
                         @Query("height") height:String,
                         @Query("activitylevel") activityLevel:String,
                         @Query("goal") goal:String
    ):Call<MacrosCountResponse>

}