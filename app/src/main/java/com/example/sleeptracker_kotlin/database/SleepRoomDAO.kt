package com.example.sleeptracker_kotlin.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepRoomDAO {

    @Insert
    fun insertNewNight(sleep: SleepingEntity)

    @Update
    fun updateNight(sleep: SleepingEntity)

    @Query("SELECT * FROM sleeping_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepingEntity>>

    @Query("SELECT * FROM sleeping_quality_table WHERE nightId = :nightId")
    fun getSingleNight(nightId: Long):SleepingEntity

    @Query("SELECT * FROM sleeping_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getLastNight(): SleepingEntity

    @Query("DELETE FROM sleeping_quality_table")
    fun deleteAll()

    @Delete
    fun deleteSpecificNight(sleep: SleepingEntity)
}