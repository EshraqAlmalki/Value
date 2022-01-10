package com.tuwaiq.value.database

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.tuwaiq.value.R

const val NOTIFICATION_CHANNEL_ID = "mine"
class ValueApp:Application() {

    override fun onCreate() {
        super.onCreate()

//        val channelName = resources.getString(R.string.notification_channe_name)
//
//
//        val channelImportance =NotificationManager.IMPORTANCE_DEFAULT
//
//        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID
//            ,channelName,channelImportance)
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//                   notificationManager.createNotificationChannel(channel)
//

        ValueRepo.initialize(this)
    }
}