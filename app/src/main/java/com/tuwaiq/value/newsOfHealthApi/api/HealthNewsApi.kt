package com.tuwaiq.value.newsOfHealthApi.api

import com.tuwaiq.value.newsOfHealthApi.models.GetNewsResponse
import com.tuwaiq.value.newsOfHealthApi.models.HealthNews
import com.tuwaiq.value.newsOfHealthApi.models.HealthNewsItem
import retrofit2.Call
import retrofit2.http.GET

interface HealthNewsApi {

    @GET("news?rapidapi-key=8cf1095789msh07b773fe654b864p17b6a8jsn36983950c696")
    fun healthNews():Call<HealthNews>
}