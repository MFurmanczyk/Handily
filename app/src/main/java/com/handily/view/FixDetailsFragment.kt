package com.handily.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder.createSource
import android.graphics.ImageDecoder.decodeBitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.handily.R
import com.handily.databinding.FragmentFixDetailsBinding
import com.handily.model.FixRequest
import com.handily.util.GridItemDecoration
import com.handily.viewmodel.HandilyViewModel
import kotlinx.coroutines.launch


private const val  TAG = "FixDetailsFragment"

private const val CARDS_IN_ROW = 3
private const val PICK_IMAGES = 14

class FixDetailsFragment : Fragment() {

    private var _binding: FragmentFixDetailsBinding? = null
    private val binding get() = _binding!!


    private val adapter = FixPhotosRecyclerViewAdapter(arrayListOf())

    val viewModel: HandilyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        _binding = FragmentFixDetailsBinding.inflate(inflater, container, false)

        observeViewModel()

        binding.fixPhotosList.layoutManager = GridLayoutManager(context, CARDS_IN_ROW, RecyclerView.VERTICAL, false)
        binding.fixPhotosList.adapter = adapter
        binding.fixPhotosList.addItemDecoration(GridItemDecoration(this.requireContext(), R.dimen.padding_2))

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

        binding.addPhotosFab.setOnClickListener {
            selectPhotos()
        }

    }

    //TODO: Zrefaktorować
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


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGES){
            viewModel.getFixPhotos(data)
            adapter.notifyDataSetChanged()
        }
    }

    private fun selectPhotos() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGES);
    }


    private fun observeViewModel() {
        viewModel.fixRequestPhotos.observe(viewLifecycleOwner) {
            adapter.updatePhotosList(it)
        }
    }
}