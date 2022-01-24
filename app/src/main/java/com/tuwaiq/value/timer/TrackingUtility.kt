package com.tuwaiq.value.timer

import android.content.Context
import android.location.Location
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit

object TrackingUtility {

    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            EasyPermissions.hasPermissions(context
            , android.Manifest.permission.ACCESS_FINE_LOCATION
            , android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }else{
            EasyPermissions.hasPermissions(context
                , android.Manifest.permission.ACCESS_FINE_LOCATION
                , android.Manifest.permission.ACCESS_COARSE_LOCATION
            , android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }


    fun FormattedStopWatchTime(ms: Long, includeMillis:Boolean = false): String{
        var milliSeconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds)
        milliSeconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
        if (!includeMillis){
            return "${if (hours < 10) "0" else ""}$hours: " +
                    "${if (minutes < 10)"0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"
        }
        milliSeconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliSeconds /= 10
        return "${if (hours < 10) "0" else ""}$hours: " +
                "${if (minutes < 10)"0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds" +
                "${if (milliSeconds < 10 ) "0" else ""}$milliSeconds"

    }

    fun calculatePolylineLength(polyline: Polyline): Float{
        var distance = 0f

        for (i in 0..polyline.size-2){
            val position1 = polyline[i]
            val position2 = polyline[i+1]

            val result = FloatArray(1)
            Location.distanceBetween(
                position1.latitude,
                position1.longitude,
                position2.latitude,
                position2.longitude,
                result
            )
            distance += result[0]
        }

        return distance
    }
}