package com.handily.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.model.LatLng
import com.handily.databinding.FragmentFixesBinding
import com.handily.viewmodel.HandilyViewModel

class FixesFragment : Fragment() {

    private var _binding: FragmentFixesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HandilyViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFixesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as LandingActivity).checkLocationPermission()
        binding.userLocationActionButton.setOnClickListener {
            viewModel.getUserLocation()
        }
    }

    fun onPermissionResult(permissionGranted: Boolean) {
        viewModel.getUserLocation()
    }

}