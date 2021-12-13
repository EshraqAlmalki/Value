package com.tuwaiq.value.fitnessCalculator.repo

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FitnessRepo {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .build()



    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://fitness-calculator.p.rapidapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

}