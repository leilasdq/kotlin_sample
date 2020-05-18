package com.example.sleeptracker_kotlin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sleeptracker_kotlin.database.SleepRoomDAO
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class SleepQualityViewModel(private val database: SleepRoomDAO, private val sleepId: Long): ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val navigate = MutableLiveData<Boolean>()
    val isNavigate: LiveData<Boolean>
        get() = navigate

    fun doneNavigate(){
        navigate.value = null
    }

    fun onQualityClicked(quality: Int){
        uiScope.launch {
            withContext(IO){
                val tonight = database.getSingleNight(sleepId)
                tonight.quality = quality
                database.updateNight(tonight)
            }
            navigate.value = true
        }
    }
}