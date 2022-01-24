package com.tuwaiq.value.database

import android.app.Application



const val NOTIFICATION_CHANNEL_ID = "mine"



class ValueApp:Application() {

    override fun onCreate() {
        super.onCreate()



        ValueRepo.initialize(this)
    }
}