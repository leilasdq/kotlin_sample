package com.example.sleeptracker_kotlin.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sleeptracker_kotlin.database.SleepRoomDAO
import java.lang.Exception

class SleepTrackerViewModelFactory(private val app: Application, private val database: SleepRoomDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepTrackerViewModel::class.java)){
            return SleepTrackerViewModel(app, database) as T
        }
        throw Exception("Unknown error happened..")
    }
}