package com.example.sleeptracker_kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeptracker_kotlin.database.SleepingEntity
import com.example.sleeptracker_kotlin.databinding.ListItemSleepNightBinding

class SleepingAdapter(private val clickListener: SleepNightListener) : ListAdapter<SleepingEntity, SleepingAdapter.SleepingViewHolder>(SleepingDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepingViewHolder {
        return SleepingViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SleepingViewHolder, position: Int) {
        holder.bind(clickListener,getItem(position)!!)
    }

    class SleepingViewHolder private constructor(private val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SleepNightListener, sleep: SleepingEntity){
            binding.sleeping = sleep
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): SleepingViewHolder {
                val binding = ListItemSleepNightBinding.
                    inflate(LayoutInflater.from(parent.context), parent, false)

                return SleepingViewHolder(binding)
            }
        }
    }
}

class SleepingDiffUtil : DiffUtil.ItemCallback<SleepingEntity>() {
    override fun areItemsTheSame(oldItem: SleepingEntity, newItem: SleepingEntity): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepingEntity, newItem: SleepingEntity): Boolean {
        return newItem == oldItem
    }
}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepingEntity) = clickListener(night.nightId)
}