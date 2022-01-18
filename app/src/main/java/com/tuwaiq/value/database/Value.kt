package com.tuwaiq.value.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Value(


    var weight:String ="",
    var weightGoal:String="",
    var height:String ="",
    var name:String ="",
    var password:String ="",
    @PrimaryKey
    var email:String ="",
    var active:String= "",
    var steps:String= "",
//    var calories:String= "",
    var stGoal:String="",
    var calGoal:String= "",
    var gender:String="",
    var age:String="",
    var fat:String ="",
    var carb:String ="",
    var protein:String ="",
    var calor:String= "",
    var documentId:String=""
)