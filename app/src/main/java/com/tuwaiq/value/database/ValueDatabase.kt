package com.tuwaiq.value.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Value::class], version = 1)
abstract class ValueDatabase:RoomDatabase() {

abstract fun valueDao():ValueDao

}