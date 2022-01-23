package com.tuwaiq.value.timer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.tuwaiq.value.MainActivity
import com.tuwaiq.value.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
const val NOTIFICATION_CHANNEL_ID ="tracking_channel"
const val NOTIFICATION_CHANNEL_NAME ="Tracking"
const val NOTIFICATION_ID =1
const val ACTION_SHOW_TIMER_FRAGMENT="ACTION_SHOW_TIMER_FRAGMENT"
const val TIMER_UPDATED = "time updated"
const val TIMER_EXTRA = "timer extra"
const val LOCATION_UPDATE_INTERVAL = 5000L
const val FASTER_LOCATION_INTERVAL = 2000L
const val TIMER_UPDATE_INTERVAL = 50L

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>
class TrackingService: LifecycleService() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var timeRunInSec = MutableLiveData<Long>()

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecTimestamp = 0L

    fun startTimer(){
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!){
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInMil.postValue(timeRun + lapTime)
                if (timeRunInMil.value!! >= lastSecTimestamp + 1000L){
                    timeRunInSec.postValue(timeRunInSec.value!! + 1)
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }


    companion object{

        val timeRunInMil = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSec.postValue(0L)
        timeRunInMil.postValue(0L)
    }


    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this , androidx.lifecycle.Observer {
            updateLocationTracking(it)
        })
    }


    var isFirstRun = true




    private fun pauseService(){
        isTracking.postValue(false)
        isTimerEnabled = false
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else{
                        Timber.d("Resuming service...")
                       startTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("paused service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("stopped service")
                }
            }
        }


        return super.onStartCommand(intent, flags, startId)

    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking:Boolean){
        if (isTracking){
            if (TrackingUtility.hasLocationPermissions(this)){
                val request = com.google.android.gms.location.LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTER_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,locationCallback, Looper.getMainLooper()
                )
            }
        }else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            if (isTracking.value!!){
                p0?.locations?.let {
                    for (location in it ){
                        addPathPoint(location)
                        Timber.d("NEW LOCATION : ${location.altitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?){
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))





    private fun startForegroundService(){

        startTimer()
        addEmptyPolyline()
        isTracking.postValue(true)

        val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotification(notificationManger)
        }

        val notificationBuilder = NotificationCompat.Builder(this , NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_av_timer_24)
            .setContentTitle("Running")
            .setContentText("00:00:00")
            //.setContentIntent(getMainActivityPendingIntent())
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this , 0 , Intent(this , MainActivity::class.java).also {
            it.action = ACTION_SHOW_TIMER_FRAGMENT
        }, FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(notificationManager: NotificationManager){
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID
            ,NOTIFICATION_CHANNEL_NAME
            ,IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }
}