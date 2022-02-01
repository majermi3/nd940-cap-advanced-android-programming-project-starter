package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.MyApp
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.ServiceLocator
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
    private val _viewModel by viewModels<ElectionsViewModel>() {
        ElectionsViewModelFactory(
                requireContext().applicationContext as MyApp,
                (requireContext().applicationContext as MyApp).electionsRepository
        )
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.electionsViewModel = _viewModel

        val adapter = ElectionListAdapter(ElectionListener { election ->
            navigateToVoterInfo(election)
        })
        binding.upcomingElectionsList.adapter = adapter
        binding.upcomingElectionsList.layoutManager = LinearLayoutManager(requireContext())

        _viewModel.upcomingElections.observe(viewLifecycleOwner, Observer { elections ->
            adapter.submitList(elections)
        })

        return binding.root
    }

    private fun navigateToVoterInfo(election: Election) {
        findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election.id,
                        election.division
                )
        )
    }

    //TODO: Refresh adapters when fragment loads

}