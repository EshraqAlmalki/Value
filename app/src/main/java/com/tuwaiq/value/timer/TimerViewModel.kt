package com.tuwaiq.value.timer

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tuwaiq.value.dataStore.DatastorePreferences
import kotlinx.coroutines.launch


class TimerViewModel(private val app: Application,context: Context): AndroidViewModel(app) {

    private val datastore = DatastorePreferences(app)

    val getTimer = datastore.timerSetting

    val timer : MutableLiveData<Boolean> = MutableLiveData()

    fun datastoreTimer(context: Context,timerIsUp:Boolean){
        viewModelScope.launch {

            datastore.saveUserTimer(context,timerIsUp)
        }.invokeOnCompletion {
            timer.value = true
        }
    }


//

}