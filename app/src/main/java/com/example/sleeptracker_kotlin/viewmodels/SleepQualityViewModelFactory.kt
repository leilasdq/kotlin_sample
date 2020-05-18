package com.example.sleeptracker_kotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sleeptracker_kotlin.database.SleepRoomDAO
import java.lang.Exception

class SleepQualityViewModelFactory(private val database: SleepRoomDAO, private val sleepId: Long) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityViewModel::class.java)){
            return SleepQualityViewModel(database, sleepId) as T
        }
        throw Exception("Unknown error happened..")
    }
}