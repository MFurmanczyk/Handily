package com.handily.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.handily.R
import com.handily.databinding.FragmentMapsBinding
import com.handily.viewmodel.HandilyViewModel


class MapsFragment : Fragment() {

    private val viewModel: HandilyViewModel by activityViewModels()

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    private var location: LatLng = LatLng(0.0, 0.0)


    private val onLocationPermissionGranted = OnMapReadyCallback { googleMap ->

        map = googleMap
        observeViewModel()
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5.0F))
        map.setOnCameraMoveListener {
            location = map.cameraPosition.target
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapToolbar.inflateMenu(R.menu.fix_map_menu)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(onLocationPermissionGranted)

        binding.mapToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.fix_map_next -> {
                    viewModel.setLocation(location)
                    val action = FixesFragmentDirections.actionFixesFragmentToFixDetailsFragment()
                    findNavController().navigate(action)
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }

    }

    private fun observeViewModel() {
        viewModel.userLocation.observe(viewLifecycleOwner) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 20.0F))
            location = it
        }
    }
}