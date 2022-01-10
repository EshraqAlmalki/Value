package com.tuwaiq.value.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastorePreferences(context: Context) {

     private val Context.dataStore : DataStore<Preferences> by
     preferencesDataStore(name = "setting")

    suspend fun saveUserSetting(context: Context,englishLanguage:Boolean){


        context.dataStore.edit { setting ->
            setting[LANGUAGE_SETTING_KEY] = englishLanguage

        }
    }



    suspend fun saveUserTimer(context: Context,timerIsUp:Boolean){
        context.dataStore.edit { setTimer ->
            setTimer[TIMER_KEY] = timerIsUp
        }
    }

    val timerSetting :Flow<Boolean> = context.dataStore.data.map {
        val timerMap = it[TIMER_KEY] ?: false
        timerMap
    }

    val languageMode : Flow<Boolean> = context.dataStore.data.map {
        val modeMap = it[LANGUAGE_SETTING_KEY] ?: false
        modeMap
    }

    companion object {
        private val LANGUAGE_SETTING_KEY = booleanPreferencesKey("language-mode")
        private val TIMER_KEY = booleanPreferencesKey("timer-is-up")

    }


}