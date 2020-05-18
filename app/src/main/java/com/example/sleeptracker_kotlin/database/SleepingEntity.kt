package com.example.sleeptracker_kotlin.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleeping_quality_table")
data class SleepingEntity (
    @PrimaryKey(autoGenerate = true)
    var nightId: Long = 0L,
    @ColumnInfo(name = "start_night")
    val startNight: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_night")
    var endTime: Long = startNight,
    @ColumnInfo(name = "quality_rating")
    var quality: Int = -1
)