package com.tuwaiq.value.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Value::class, Run::class], version = 1)

@TypeConverters(RunConverters::class)
abstract class ValueDatabase:RoomDatabase() {

abstract fun valueDao():ValueDao

}