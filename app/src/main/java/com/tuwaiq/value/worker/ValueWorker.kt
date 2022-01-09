package com.tuwaiq.value.worker

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tuwaiq.value.MainActivity
import com.tuwaiq.value.R
import com.tuwaiq.value.database.NOTIFICATION_CHANNEL_ID

class ValueWorker(private val context: Context , workerParameters: WorkerParameters)
    :Worker(context,workerParameters) {
    override fun doWork(): Result {


        val resource = context.resources

        val intent = MainActivity.intent(context)
        val pendingIntent = PendingIntent.getActivity(context,0,intent
            ,PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(
            context , NOTIFICATION_CHANNEL_ID
        ).setTicker(resource.getString(R.string.timer_end))
            .setSmallIcon(R.drawable.ic_baseline_av_timer_24)
            .setContentTitle(resource.getString(R.string.timer_ends_title))
            .setContentText(resource.getString(R.string.good_jod))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        showNotification(notification)

        return Result.success()
    }

    fun showNotification(notification: Notification){
        val intent = Intent(SHOW_NOTIFICATION).apply {
            putExtra(NOTIFICATION , notification)
        }
        context.sendOrderedBroadcast(intent , PERM_PRIVATE)
    }


    companion object{
        val SHOW_NOTIFICATION = "com.tuwaiq.value.SHOW_NOTIFICATION"
        val PERM_PRIVATE = "com.tuwaiq.value.PRIVATE"
        val NOTIFICATION = "timer-notification"
    }
}