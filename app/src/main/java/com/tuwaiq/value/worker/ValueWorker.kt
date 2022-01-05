//package com.tuwaiq.value.worker
//
//import android.app.Notification
//import android.content.Context
//import android.content.Intent
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//import com.tuwaiq.value.ValueSharedPreferences
//import com.tuwaiq.value.database.Value
//
//
//class ValueWorker(private val context: Context , params: WorkerParameters):Worker(context,params){
//    override fun doWork(): Result {
//
//
//
//       // val steps = ValueSharedPreferences.getStepsCounter(context)
//        val timer = ValueSharedPreferences.getTimer(context)
//
//
//
//
//        return Result.success()
//    }
//
//    fun showBackgroundNotifications(notification: Notification){
//
//        val intent =Intent(NOTIFICATION_ACTION).apply {
//            putExtra(NOTIFICATION_KEY, notification)
//        }
//
//        context.sendOrderedBroadcast(intent , NOTIFICATION_PERM)
//    }
//
//    companion object{
//        const val NOTIFICATION_KEY = "NOTIFICATION_KEY"
//        const val NOTIFICATION_ACTION = "com.tuwaiq.value.SHOW_NOTIFICATION"
//        const val NOTIFICATION_PERM = "com.tuwaiq.value.PRIVATE"
//    }
//}
