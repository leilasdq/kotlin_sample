package com.example.sleeptracker_kotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepingEntity::class], version = 1, exportSchema = false)
abstract class DatabaseHelper : RoomDatabase() {
    abstract val sleepRoomDAO: SleepRoomDAO

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper? {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, DatabaseHelper::class.java, "sleeping_quality_helper")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}