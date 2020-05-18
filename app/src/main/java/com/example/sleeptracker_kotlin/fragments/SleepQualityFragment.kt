package com.example.sleeptracker_kotlin.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.sleeptracker_kotlin.R
import com.example.sleeptracker_kotlin.database.DatabaseHelper
import com.example.sleeptracker_kotlin.database.SleepRoomDAO
import com.example.sleeptracker_kotlin.databinding.FragmentSleepQualityBinding
import com.example.sleeptracker_kotlin.viewmodels.SleepQualityViewModel
import com.example.sleeptracker_kotlin.viewmodels.SleepQualityViewModelFactory
import com.example.sleeptracker_kotlin.viewmodels.SleepTrackerViewModel
import com.example.sleeptracker_kotlin.viewmodels.SleepTrackerViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class SleepQualityFragment : Fragment() {
    private lateinit var binding: FragmentSleepQualityBinding
    private lateinit var application: Application
    private lateinit var viewModel: SleepQualityViewModel
    private lateinit var viewModelFactory: SleepQualityViewModelFactory
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sleepRoomDAO: SleepRoomDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_quality, container, false)

        application = requireNotNull(this.activity).application
        databaseHelper = DatabaseHelper.getInstance(application)!!
        sleepRoomDAO = databaseHelper.sleepRoomDAO
        viewModelFactory = SleepQualityViewModelFactory(sleepRoomDAO, SleepQualityFragmentArgs.fromBundle(arguments!!).sleepNightKey)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SleepQualityViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.isNavigate.observe(viewLifecycleOwner, Observer { isNavigate ->
            if (isNavigate == true){
                this.findNavController().navigate(
                    SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())

                viewModel.doneNavigate()
            }
        })

        return binding.root
    }

}
