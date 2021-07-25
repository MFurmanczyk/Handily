package com.handily.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.handily.R
import com.handily.databinding.FragmentHomeBinding
import com.handily.util.GridItemDecoration

private const val CARDS_IN_ROW = 2

class FindOrdersFragment : HomeFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    override val adapter = FixRequestCardRecyclerViewAdapter(arrayListOf(), false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setDummyLocation()
        observeViewModel()

        binding.userFixRequestsList.layoutManager = GridLayoutManager(context, CARDS_IN_ROW, RecyclerView.VERTICAL, false)
        binding.userFixRequestsList.adapter = adapter
        binding.userFixRequestsList.addItemDecoration(GridItemDecoration(this.requireContext(), R.dimen.padding_4))

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFixRequests(viewModel.userLocation.value!!,sharedPreferences.getInt("radius_key",10))
            binding.swipeRefresh.isRefreshing = false
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
        return binding.root
    }

    override fun observeViewModel() {
        viewModel.authenticatedUser.observe(viewLifecycleOwner) {
            it?.let {
                binding.user = it
            }
            viewModel.userLocation.value?.let {
                viewModel.getFixRequests(it, sharedPreferences.getInt("radius_key",10))
            }
        }

        viewModel.fixRequest.observe(viewLifecycleOwner) {
            adapter.updateFixRequestList(it)
        }
    }

    //for tests (Katowice) :)
    private fun setDummyLocation(){
        if(viewModel.userLocation.value==null) {
            viewModel.setLocation(LatLng(50.25, 19.0))
        }
    }

}