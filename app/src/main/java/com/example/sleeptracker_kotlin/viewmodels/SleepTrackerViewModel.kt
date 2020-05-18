package com.example.sleeptracker_kotlin.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.sleeptracker_kotlin.database.SleepRoomDAO
import com.example.sleeptracker_kotlin.database.SleepingEntity
import com.example.sleeptracker_kotlin.formatNights
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepTrackerViewModel(private val app: Application, private val database: SleepRoomDAO) :
    ViewModel() {
    private val viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Main + viewModelJob)
    private var tonight = MutableLiveData<SleepingEntity>()
    var allNights = database.getAllNights()
    var allNightString = Transformations.map(allNights) { nights ->
        formatNights(nights, app.resources)
    }

    //If true it means that sleep is recording so start should be invisible and stop visible
    private val buttonVisibility = MutableLiveData<Boolean>()
    val isSleepRecording: LiveData<Boolean>
        get() = buttonVisibility
    fun setStartVisible(){
        buttonVisibility.value = false
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepingEntity>()
    val navigateToSleepQuality: LiveData<SleepingEntity>
        get() = _navigateToSleepQuality

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    private val snackbar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean>
        get() = snackbar
    fun showingSnackIsDone(){
        snackbar.value = false
    }


    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepingEntity? {
        var night: SleepingEntity? = null
        withContext(IO) {
            night = database.getLastNight()
            if (night?.endTime != night?.startNight) {
                night = null
            }
        }
        return night
    }

    private suspend fun clearDatabase() {
        withContext(IO) {
            database.deleteAll()
        }
    }

    private suspend fun insertNight(night: SleepingEntity) {
        withContext(IO) {
            database.insertNewNight(night)
        }
    }

    private suspend fun updateNight(night: SleepingEntity) {
        withContext(IO) {
            database.updateNight(night)
        }
    }

    private suspend fun deleteOneNight(id: Long){
        withContext(IO){
            database.deleteSpecificNight(database.getSingleNight(id))
        }
    }

    //OnStart Button Clicked
    fun onStartClick() {
        uiScope.launch {
            val newNight = SleepingEntity()
            insertNight(newNight)

            tonight.value = getTonightFromDatabase()

            buttonVisibility.value = true
        }
    }

    //OnStop Button Clicked
    fun onStopClick() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTime = System.currentTimeMillis()
            updateNight(oldNight)

            // Set state to navigate to the SleepQualityFragment.
            _navigateToSleepQuality.value = oldNight
            buttonVisibility.value = false
        }

    }

    //OnClear Button Clicked
    fun onClearClicked() {
        uiScope.launch {
            clearDatabase()
            tonight.value = null

            snackbar.value = true
        }
    }

    fun onItemSelected(id: Long){
        uiScope.launch {
            deleteOneNight(id)
        }
    }

    //called when view model destroy
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}