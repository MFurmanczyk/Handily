package com.handily.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.handily.R
import com.handily.databinding.FragmentFixOverviewBinding
import com.handily.databinding.FragmentFixPhotoBinding
import com.handily.util.getProgressDrawable
import com.handily.util.loadImage
import com.handily.viewmodel.FixOverviewViewModel

class FixOverviewFragment : Fragment() {

    private var _binding: FragmentFixOverviewBinding? = null
    private val binding get() = _binding!!

    private var fixUuid = ""

    private val viewModel: FixOverviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentFixOverviewBinding.inflate(inflater, container, false)

        binding.photosPager.adapter = FixPhotosPagerAdapter(this,
            arrayListOf())

        setupViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            fixUuid = FixOverviewFragmentArgs.fromBundle(it).uuid
        }

        viewModel.setFixRequest(fixUuid)
    }

    private fun setupViewModel() {
        viewModel.fixRequest.observe(viewLifecycleOwner) {
            binding.fixRequest = it
            (binding.photosPager.adapter as FixPhotosPagerAdapter).setBitmapList(it.imagesUrls!!)
        }
    }
}