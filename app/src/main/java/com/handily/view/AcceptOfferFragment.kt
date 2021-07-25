package com.handily.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.handily.databinding.FragmentAcceptOfferBinding
import com.handily.viewmodel.OfferViewModel


class AcceptOfferFragment(private val fixUuid: String) : Fragment() {

    private var _binding: FragmentAcceptOfferBinding? = null
    private val binding get() = _binding!!

    private val adapter = FixOfferAcceptanceRecycleViewAdapter(arrayListOf())

    private val viewModel: OfferViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAcceptOfferBinding.inflate(inflater, container, false)
        binding.offerList.adapter = adapter

        viewModel.setFixOfferList(fixUuid)

        return binding.root
    }

    private fun setupViewModel() {
        viewModel.fixOfferList.observe(viewLifecycleOwner) {
            adapter.updateFixRequestList(it)
        }
    }

}