package com.tuwaiq.value.timer

import androidx.lifecycle.ViewModel
import com.tuwaiq.value.database.ValueRepo

class RunTimelineViewModel: ViewModel(){

    private val valueRepo = ValueRepo.get()


   val getAllActivities = valueRepo.getAllActivities()
}