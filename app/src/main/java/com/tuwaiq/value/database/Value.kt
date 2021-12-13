package com.tuwaiq.value.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Value(

    @PrimaryKey
    var weight:String ="",
    var weightGoal:String="",
    var height:String ="",
    val name:String ="",
    var password:String ="",
    var email:String ="",
    var active:String= "",
    var steps:String= "",
    var calories:String= "",
    var stGoal:String="",
    var calGoal:String= "",
    var gender:String="",
    var age:String=""


)