package com.handily.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.handily.R
import com.handily.databinding.FragmentFixOverviewBinding
import com.handily.viewmodel.FixOverviewViewModel

class FixOverviewFragment : Fragment() {

    private var _binding: FragmentFixOverviewBinding? = null
    private val binding get() = _binding!!

    private var fixUuid = ""
    private var isClient = true

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
            isClient = FixOverviewFragmentArgs.fromBundle(it).isClient
        }

        parentFragmentManager.commit {
            val fragment =  if(isClient) AcceptOfferFragment(fixUuid) else MakeOfferFragment()

            replace(R.id.fragment_offer, fragment)
            setReorderingAllowed(true)
            addToBackStack(null)
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