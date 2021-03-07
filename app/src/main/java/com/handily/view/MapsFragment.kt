package com.handily.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.handily.R
import com.handily.databinding.FragmentMapsBinding

private const val LOC = "loc"

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var location: LatLng? = null

    private val callback = OnMapReadyCallback { googleMap ->

        if(location == null) {
            location = LatLng(0.0, 0.0)
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.setOnCameraMoveListener {
            location = googleMap.cameraPosition.target
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LOC, location)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        location = savedInstanceState?.getParcelable(LOC)
        super.onViewStateRestored(savedInstanceState)
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
        mapFragment?.getMapAsync(callback)
        binding.mapToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.fix_map_next -> {
                    if(location != null) {
                        val action = FixesFragmentDirections.actionFixesFragmentToFixDetailsFragment(location!!)
                        findNavController().navigate(action)
                    }
                    return@setOnMenuItemClickListener  true
                }
                else -> {
                    return@setOnMenuItemClickListener  false
                }
            }
        }

    }
}