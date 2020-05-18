package com.example.sleeptracker_kotlin.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.sleeptracker_kotlin.R
import com.example.sleeptracker_kotlin.convertDurationToFormatted
import com.example.sleeptracker_kotlin.convertNumericQualityToString
import com.example.sleeptracker_kotlin.database.SleepingEntity

@BindingAdapter("sleepDuration")
fun TextView.setSleepDuration(item: SleepingEntity){
    text = convertDurationToFormatted(item.startNight, item.endTime, context.resources)
}

@BindingAdapter("sleepQuality")
fun TextView.setSleepQuality(item: SleepingEntity){
    text = convertNumericQualityToString(item.quality, context.resources)
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepingEntity){
    setImageResource(when (item.quality) {
        0 -> R.drawable.ic_sleep_0
        1 -> R.drawable.ic_sleep_1
        2 -> R.drawable.ic_sleep_2
        3 -> R.drawable.ic_sleep_3
        4 -> R.drawable.ic_sleep_4
        5 -> R.drawable.ic_sleep_5
        else -> R.drawable.ic_sleep_active
    })
}