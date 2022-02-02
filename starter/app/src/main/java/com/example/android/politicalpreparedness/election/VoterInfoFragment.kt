package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.MyApp
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseLocationFragment
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : BaseLocationFragment() {

    private val args: VoterInfoFragmentArgs by navArgs()

    private val _viewModel by viewModels<VoterInfoViewModel>() {
        VoterInfoViewModelFactory(
                requireContext().applicationContext as MyApp,
                (requireContext().applicationContext as MyApp).electionsRepository
        )
    }

    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        checkPermissionsAndSetCurrentLocation()

        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.voterInfoViewModel = _viewModel

        binding.stateLocations.setOnClickListener {
            _viewModel.voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl?.let {
                url -> openUrl(url)
            }
        }
        binding.stateBallot.setOnClickListener {
            _viewModel.voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl?.let {
                url -> openUrl(url)
            }
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

    private fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onLocationSet() {
        _viewModel.loadVoterInfo(args.argElectionId, geoCodeLocation(currentLocation))
    }

    override fun showLocationPermissionError() {
        Snackbar.make(
                binding.voterInfoContainer,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
        )
                .setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
    }
}