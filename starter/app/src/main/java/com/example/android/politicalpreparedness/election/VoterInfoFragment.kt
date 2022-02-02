package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.MyApp
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseLocationFragment
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : BaseLocationFragment() {

    private val args: VoterInfoFragmentArgs by navArgs()

    private val _viewModel by viewModels<VoterInfoViewModel>() {
        VoterInfoViewModelFactory(
                requireContext().applicationContext as MyApp,
                (requireContext().applicationContext as MyApp).electionsRepository
        )
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
        binding.saveElectionButton.setOnClickListener {
            _viewModel.saveElection()
        }

        _viewModel.savedElection.observe(viewLifecycleOwner, Observer { election ->
            if (election != null) {
                binding.saveElectionButton.text = getString(R.string.unfollow_election)
            } else {
                binding.saveElectionButton.text = getString(R.string.follow_election)
            }
        })

        return binding.root
    }

    override fun onLocationSet() {
        _viewModel.loadVoterInfo(args.argElectionId, geoCodeLocation(currentLocation))
    }

    override fun showLocationPermissionError() {
        // TODO show snackbar
    }

    //TODO: Create method to load URL intents

}