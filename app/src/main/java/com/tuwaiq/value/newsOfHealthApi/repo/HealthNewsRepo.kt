package com.tuwaiq.value.newsOfHealthApi.repo

import android.content.ContentValues.TAG
import android.util.Log
import com.tuwaiq.value.newsOfHealthApi.api.HealthNewsApi

import com.tuwaiq.value.newsOfHealthApi.models.HealthNewsItem

import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class HealthNewsRepo {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://live-fitness-and-health-news.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(HealthNewsApi::class.java)

    suspend fun getAllNews():List<HealthNewsItem?>{

        var newsList:List<HealthNewsItem?> = emptyList()

        val response = api.healthNews().awaitResponse()

        if (response.isSuccessful){
          newsList = response.body()?.toList().orEmpty()


            Log.e(TAG, "getAllNews: $newsList" )
        }else{

            Log.e(TAG, "geAllNews: the error is ${response.errorBody()}" )
        }

       return newsList

    }

}