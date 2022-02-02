package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.MyApp
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseLocationFragment
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.material.snackbar.Snackbar

class RepresentativeFragment : BaseLocationFragment() {

    private val _viewModel by viewModels<RepresentativeViewModel>() {
        RepresentativeViewModelFactory(requireContext().applicationContext as MyApp)
    }

    private lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.representativeViewModel = _viewModel

        setUpStateSpinner()

        val adapter = RepresentativeListAdapter()
        binding.representativeList.adapter = adapter
        _viewModel.representatives.observe(viewLifecycleOwner, Observer { representatives ->
            adapter.submitList(representatives)
        })

        binding.buttonSearch.setOnClickListener {
            _viewModel.findRepresentatives()
            hideKeyboard()
        }
        binding.buttonLocation.setOnClickListener {
            checkPermissionsAndSetCurrentLocation()
            hideKeyboard()
        }

        return binding.root
    }

    private fun setUpStateSpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.states))
        binding.state.adapter = adapter
    }

    override fun onLocationSet() {
        val address = geoCodeLocation(currentLocation)
        binding.addressLine1.setText(address.line1)
        binding.zip.setText(address.zip)
        binding.city.setText(address.city)
        binding.state.setSelection(resources.getStringArray(R.array.states).indexOf(address.state))
    }

    override fun showLocationPermissionError() {
        Snackbar.make(
                binding.representativeContainer,
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

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}