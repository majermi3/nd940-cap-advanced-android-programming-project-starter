package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.MyApp
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election
import timber.log.Timber

class ElectionsFragment: Fragment() {

    private val _viewModel by viewModels<ElectionsViewModel>() {
        ElectionsViewModelFactory(
                requireContext().applicationContext as MyApp,
                (requireContext().applicationContext as MyApp).electionsRepository
        )
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.electionsViewModel = _viewModel

        setUpList(_viewModel.upcomingElections, binding.upcomingElectionsList)
        setUpList(_viewModel.savedElections, binding.savedElectionsList)

        return binding.root
    }

    private fun setUpList(elections: LiveData<List<Election>>, recyclerView: RecyclerView) {
        val adapter = ElectionListAdapter(ElectionListener { election ->
            navigateToVoterInfo(election)
        })
        recyclerView.adapter = adapter

        elections.observe(viewLifecycleOwner, Observer { newElections ->
            adapter.submitList(newElections)
        })
    }

    private fun navigateToVoterInfo(election: Election) {
        findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election.id,
                        election.division
                )
        )
    }

    override fun onResume() {
        super.onResume()
        _viewModel.loadSavedElections()
    }
}