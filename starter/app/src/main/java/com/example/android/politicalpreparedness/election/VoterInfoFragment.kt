package com.example.android.politicalpreparedness.election

import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.MyApp
import com.example.android.politicalpreparedness.base.BaseLocationFragment
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : BaseLocationFragment() {

    private val args: VoterInfoFragmentArgs by navArgs()

    private val _viewModel by viewModels<VoterInfoViewModel>() {
        VoterInfoViewModelFactory(requireContext().applicationContext as MyApp)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.voterInfoViewModel = _viewModel

        binding.stateLocations.setOnClickListener {

        }
        binding.stateBallot.setOnClickListener {

        }

        return binding.root
    }

    override fun setCurrentLocation(currentLocation: Location) {
        _viewModel.loadVoterInfo(args.argElectionId, geoCodeLocation(currentLocation))
    }

    override fun showLocationPermissionError() {
        // TODO show snackbar
    }

    //TODO: Create method to load URL intents

}