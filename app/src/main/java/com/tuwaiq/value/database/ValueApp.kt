package com.tuwaiq.value.database

import android.app.Application

class ValueApp:Application() {

    override fun onCreate() {
        super.onCreate()


        ValueRepo.initialize(this)
    }
}