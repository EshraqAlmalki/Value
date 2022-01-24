package com.tuwaiq.value.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Run-model-class")
data class Run (
    var caloriesBurned :Int = 0,
    var timeInMilli:Long = 0L,
    var distanceInMeters: Int = 0,
    var avgSpeedInKMH: Float = 0F,
    var timestamp:Long = 0L,
    var imgL: Bitmap? = null


        ){
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

}