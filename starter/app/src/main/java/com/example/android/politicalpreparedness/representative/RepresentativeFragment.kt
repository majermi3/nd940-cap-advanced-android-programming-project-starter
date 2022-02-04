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
            _viewModel.findRepresentatives(getFormattedAddress()) {
                startAnimation()
            }
            hideKeyboard()
        }
        binding.buttonLocation.setOnClickListener {
            checkPermissionsAndSetCurrentLocation()
            hideKeyboard()
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

            _viewModel.findRepresentatives(getFormattedAddress()) {
                startAnimation()
            }
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val motionLayoutState = savedInstanceState?.getInt(MOTION_LAYOUT_STATE) ?: -1
        if (motionLayoutState > -1) {
            binding.representativeContainer.transitionToState(motionLayoutState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOTION_LAYOUT_STATE, binding.representativeContainer.currentState)
    }

    private fun startAnimation() {
        binding.representativeContainer.transitionToEnd()
    }

    companion object {
        const val MOTION_LAYOUT_STATE = "MOTION_LAYOUT_STATE"
    }
}