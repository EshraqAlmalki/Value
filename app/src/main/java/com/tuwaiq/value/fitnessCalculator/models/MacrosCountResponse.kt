package com.tuwaiq.value.fitnessCalculator.models

import com.google.gson.annotations.SerializedName

class MacrosCountResponse {


    @SerializedName("Data")
    lateinit var count:List<MacrosCount>
}