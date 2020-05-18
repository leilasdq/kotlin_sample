package com.example.sleeptracker_kotlin.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

import com.example.sleeptracker_kotlin.R
import com.example.sleeptracker_kotlin.adapter.SleepNightListener
import com.example.sleeptracker_kotlin.adapter.SleepingAdapter
import com.example.sleeptracker_kotlin.database.DatabaseHelper
import com.example.sleeptracker_kotlin.database.SleepRoomDAO
import com.example.sleeptracker_kotlin.databinding.FragmentSleepTrackerBinding
import com.example.sleeptracker_kotlin.viewmodels.SleepTrackerViewModel
import com.example.sleeptracker_kotlin.viewmodels.SleepTrackerViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class SleepTrackerFragment : Fragment() {
    private lateinit var binding: FragmentSleepTrackerBinding
    private lateinit var application: Application
    private lateinit var viewModel: SleepTrackerViewModel
    private lateinit var viewModelFactory: SleepTrackerViewModelFactory
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sleepRoomDAO:SleepRoomDAO
    private lateinit var adapter: SleepingAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_tracker, container, false)

        application = requireNotNull(value = this.activity).application
        databaseHelper = DatabaseHelper.getInstance(application)!!
        sleepRoomDAO = databaseHelper.sleepRoomDAO
        viewModelFactory = SleepTrackerViewModelFactory(application, sleepRoomDAO)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = SleepingAdapter(SleepNightListener { nightId ->
            viewModel.onItemSelected(nightId)
        })
        binding.recycle.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
        binding.recycle.adapter = adapter
        viewModel.allNights.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                viewModel.doneNavigating()
            }
        })

        viewModel.isSleepRecording.observe(viewLifecycleOwner, Observer { isRecording ->
            if (isRecording == true){ //Sleep is recording
                binding.startButton.visibility = View.INVISIBLE
                binding.stopButton.visibility = View.VISIBLE
            } else { //Sleep recording is finished
                binding.startButton.visibility = View.VISIBLE
                binding.stopButton.visibility = View.INVISIBLE
            }
        })

        viewModel.showSnackbar.observe(viewLifecycleOwner, Observer { isShow ->
            if (isShow == true){
                Snackbar.make(view!!, "Your data has been cleared", Snackbar.LENGTH_SHORT).show()
                viewModel.showingSnackIsDone()
            }
        })

        return binding.root
    }

}
