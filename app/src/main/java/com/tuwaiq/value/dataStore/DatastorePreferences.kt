package com.tuwaiq.value.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastorePreferences(context: Context) {

    private val dataStore : DataStore<Preferences> =
        context.createDataStore("setting")

    suspend fun saveUserSetting(englishLanguage:Boolean){
        dataStore.edit { setting ->
            setting[LANGUAGE_SETTING_KEY] = englishLanguage

        }
    }

    suspend fun saveUserTimer(timerIsUp:Long){
        dataStore.edit { setTimer ->
            setTimer[TIMER_KEY] = timerIsUp
        }
    }

    val timerSetting :Flow<Long> = dataStore.data.map {
        val timerMap = it[TIMER_KEY] ?: 0
        timerMap
    }

    val languageMode : Flow<Boolean> = dataStore.data.map {
        val modeMap = it[LANGUAGE_SETTING_KEY] ?: false
        modeMap
    }

    companion object {
        private val LANGUAGE_SETTING_KEY = preferencesKey<Boolean>("language-mode")
        private val TIMER_KEY = preferencesKey<Long>("timer-is-up")
    }


}