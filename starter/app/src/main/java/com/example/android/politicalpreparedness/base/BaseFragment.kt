package com.example.android.politicalpreparedness.base

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    protected fun showSnackBarWithSettingsIntent(view: View, resId: Int, intent: Intent) {
        Snackbar.make(view, resId, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings) {
                    startActivity(intent)
                }.show()
    }

}