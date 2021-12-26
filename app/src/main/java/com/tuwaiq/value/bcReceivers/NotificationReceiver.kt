//package com.tuwaiq.value.bcReceivers
//
//import android.app.Activity
//import android.app.Notification
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import androidx.core.app.NotificationManagerCompat
//import com.tuwaiq.value.worker.ValueWorker
//
//class NotificationReceiver :BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//
//        if (resultCode != Activity.RESULT_OK){
//            return
//        }
//
//
//        val notification:Notification = intent?.getParcelableExtra(ValueWorker.NOTIFICATION_KEY)!!
//
//        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
//
//        if (notificationManager != null) {
//            notificationManager.notify(1 ,notification)
//        }
//    }
//}