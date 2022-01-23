package com.tuwaiq.value.timer

import android.content.Context
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
}