package com.tuwaiq.value.timer

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuwaiq.value.dataStore.DatastorePreferences
import com.tuwaiq.value.database.Run
import com.tuwaiq.value.database.ValueRepo
import kotlinx.coroutines.launch


class TimerViewModel : ViewModel() {

    private val valueRepo = ValueRepo.get()

    fun insertRun(run: Run){
        valueRepo.addRun(run)
    }

}