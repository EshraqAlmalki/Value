package com.tuwaiq.value.broadcastRec

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.tuwaiq.value.worker.ValueWorker

class NotificationRec:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (resultCode != Activity.RESULT_OK ){
            return
        }

        val notification:Notification = intent?.getParcelableExtra(ValueWorker.NOTIFICATION)!!



        val notificationManger = NotificationManagerCompat.from(context!!)

        notificationManger.notify(0,notification)
    }
}