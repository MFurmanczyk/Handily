package com.handily.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.handily.R
import com.handily.databinding.ActivityLandingBinding
import com.handily.viewmodel.HandilyViewModel

private const val PERMISSION_FINE_LOCATION = 2137

class LandingActivity : AppCompatActivity() {

    private val viewModel: HandilyViewModel by viewModels()

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navController = Navigation.findNavController(this, R.id.navigation_fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        observeViewModel()

        viewModel.setUser(Firebase.auth.currentUser?.uid.toString()) {
            when {
                Firebase.auth.currentUser == null -> {
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                }
                viewModel.authenticatedUser.value == null -> {
                    val intent = Intent(this, UserDetailsActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    findViewById<View>(R.id.navigation_fragment).visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }

        }
    }

    fun checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle("Location access")
                    .setMessage("This application needs access to Device location.")
                    .setPositiveButton("OK") { _, _ ->
                        requestLocationPermission()
                    }
                    .setNegativeButton("Cancel") {_, _ ->
                        notifyFragment(false)
                    }
                    .show()
            } else {
                requestLocationPermission()
            }
        } else {
            notifyFragment(true)
        }
    }


    private fun notifyFragment(permissionGranted: Boolean) {
        val fragment = supportFragmentManager.findFragmentById(R.id.navigation_fragment)
        val activeFragment = fragment?.childFragmentManager?.primaryNavigationFragment
        if(activeFragment is FixesFragment) {
            activeFragment.onPermissionResult(permissionGranted)
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_FINE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_FINE_LOCATION -> {
                if(permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    notifyFragment(true)
                } else {
                    notifyFragment(false)
                }
            }
        }
    }


    private fun observeViewModel() {
        viewModel.authenticatedUser.observeForever {
            it?.let { binding.user = it }
        }
    }
}