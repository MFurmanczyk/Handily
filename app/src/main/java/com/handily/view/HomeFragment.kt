package com.handily.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handily.R
import com.handily.databinding.FragmentHomeBinding
import com.handily.util.GridItemDecoration
import com.handily.viewmodel.HandilyViewModel

private const val CARDS_IN_ROW = 2

open class HomeFragment : Fragment() {

    protected var _binding: FragmentHomeBinding? = null
    protected val binding get() = _binding!!

    protected val viewModel : HandilyViewModel by activityViewModels()


    protected open val adapter = FixRequestCardRecyclerViewAdapter(arrayListOf(), true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        observeViewModel()

        binding.userFixRequestsList.layoutManager = GridLayoutManager(context, CARDS_IN_ROW, RecyclerView.VERTICAL, false)
        binding.userFixRequestsList.adapter = adapter
        binding.userFixRequestsList.addItemDecoration(GridItemDecoration(this.requireContext(), R.dimen.padding_4))

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getOwnedFixRequests(viewModel.authenticatedUser.value?.uuid.toString())
            binding.swipeRefresh.isRefreshing = false
        }

        return binding.root
    }

    protected open fun observeViewModel() {
        viewModel.authenticatedUser.observe(viewLifecycleOwner) {
            it?.let {
                binding.user = it
            }
            viewModel.authenticatedUser.value?.uuid?.let {
                viewModel.getOwnedFixRequests(it)

            }
        }

        viewModel.ownedFixRequest.observe(viewLifecycleOwner) {
            adapter.updateFixRequestList(it)
        }
    }

}