//package com.tuwaiq.value
//
//import android.content.Context
//import androidx.preference.PreferenceManager
//
//
//private const val STEPS_KEY = "steps key"
//private const val TIMER_KEY = "timer key"
//object ValueSharedPreferences {
//
//
//    fun setStepsCounter(context: Context , steps : String){
//        PreferenceManager.getDefaultSharedPreferences(context)
//            .edit().putString(STEPS_KEY ,steps )
//            .apply()
//    }
//
//
//    fun getStepsCounter(context: Context):String {
//        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
//
//        return preferenceManager.getString(STEPS_KEY , "")!!
//    }
//
//    fun setTimerAlarm(context: Context , timer:Long){
//        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TIMER_KEY ,
//            timer.toString()).apply()
//
//    }
//
//    fun getTimerAlarm(context: Context):String{
//        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
//        return preferenceManager.getString(TIMER_KEY , "")!!
//    }
//}