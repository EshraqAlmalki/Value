package com.tuwaiq.value.newsOfHealthApi.models

import com.google.gson.annotations.SerializedName

class GetNewsResponse{
   // @SerializedName("items")
   lateinit var news: List<News>
}