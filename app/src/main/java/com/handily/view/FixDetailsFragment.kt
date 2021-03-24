package com.handily.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.material.transition.MaterialContainerTransform
import com.handily.R
import com.handily.databinding.FragmentFixDetailsBinding
import com.handily.model.FixRequest
import com.handily.viewmodel.HandilyViewModel
import kotlinx.coroutines.launch


class FixDetailsFragment : Fragment() {

    private var _binding: FragmentFixDetailsBinding? = null
    private val binding get() = _binding!!

    val viewModel: HandilyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        _binding = FragmentFixDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fixDetailsToolbar.inflateMenu(R.menu.fix_details_menu)

        binding.fixDetailsToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.fix_details_post -> {
                    viewModel.viewModelScope.launch {
                        val geoHash = GeoFireUtils
                            .getGeoHashForLocation(
                                GeoLocation(
                                    viewModel.userLocation.value?.latitude!!,
                                    viewModel.userLocation.value?.longitude!!))
                        val fixRequest = FixRequest.Builder(
                            binding.fixTitle.editText?.text.toString(),
                            binding.fixDescription.editText?.text.toString()
                        )
                            .lat(viewModel.userLocation.value?.latitude!!)
                            .lng(viewModel.userLocation.value?.longitude!!)
                            .geoHash(geoHash)
                            .userUuid(viewModel.authenticatedUser.value?.uuid)
                            .build()

                        viewModel.postFixRequest(fixRequest)
                    }

                    val action =
                        FixDetailsFragmentDirections.actionFixDetailsFragmentToHomeFragment()
                    findNavController().navigate(action)
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }

    }

    private fun buildFixRequest(): FixRequest = FixRequest.Builder(
        binding.fixTitle.editText?.text.toString(),
        binding.fixDescription.editText?.text.toString()
    )
        .lat(viewModel.userLocation.value?.latitude!!)
        .lng(viewModel.userLocation.value?.longitude!!)
        .geoHash(GeoFireUtils
            .getGeoHashForLocation(
                GeoLocation(
                    viewModel.userLocation.value?.latitude!!,
                    viewModel.userLocation.value?.longitude!!)
            )
        ).userUuid(viewModel.authenticatedUser.value?.uuid)
        .build()
}