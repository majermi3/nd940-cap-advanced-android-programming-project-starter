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
import com.example.android.politicalpreparedness.representative.adapter.setNewValue

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
            _viewModel.findRepresentatives(getFormattedAddress())
            hideKeyboard()
            startAnimation()
        }
        binding.buttonLocation.setOnClickListener {
            checkPermissionsAndSetCurrentLocation()
            hideKeyboard()
        }

        if(savedInstanceState?.getBoolean(IS_FORM_SUBMITTED, false) == true) {
            _viewModel.findRepresentatives(getFormattedAddress())
            if(savedInstanceState.getBoolean(IS_ANIMATED, false)) {
                startAnimation()
            }
        }
        _viewModel.noInternetAccess.observe(viewLifecycleOwner, Observer { noInternetAccess ->
            if (noInternetAccess) {
                showSnackBarWithSettingsIntent(
                        binding.representativeContainer,
                        R.string.no_internet_access,
                        Intent().apply {
                            action = Settings.ACTION_WIFI_SETTINGS
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                )
            }
        })

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
        if (_viewModel.hasInternetConnection()) {
            val address = geoCodeLocation(currentLocation)
            binding.addressLine1.setText(address.line1)
            binding.zip.setText(address.zip)
            binding.city.setText(address.city)
            binding.state.setNewValue(address.state)

            _viewModel.findRepresentatives(getFormattedAddress())
            startAnimation()
        }
    }

    private fun getFormattedAddress(): String {
        return "${binding.addressLine1.text} ${binding.addressLine2.text}, " +
                "${binding.zip.text} ${binding.city.text}, " +
                "${binding.state.selectedItem}"
    }

    override fun showLocationPermissionError() {
        showSnackBarWithSettingsIntent(
                binding.representativeContainer,
                R.string.permission_denied_explanation,
                Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
        )
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_FORM_SUBMITTED, _viewModel.representatives.value != null)
        outState.putBoolean(IS_ANIMATED, binding.representativeContainer.progress == 1f)
    }

    private fun startAnimation() {
        binding.representativeContainer.transitionToEnd()
    }

    companion object {
        const val IS_FORM_SUBMITTED = "IS_FORM_SUBMITTED"
        const val IS_ANIMATED = "IS_ANIMATED"
    }
}