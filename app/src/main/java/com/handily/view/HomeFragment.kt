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
import com.handily.model.FixRequest
import com.handily.model.GridItemDecoration
import com.handily.viewmodel.HandilyViewModel

private const val CARDS_IN_ROW = 2

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HandilyViewModel by activityViewModels()

    private var fixRequestsList : List<FixRequest> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        observeViewModel()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeTopAppBar.inflateMenu(R.menu.home_menu)
        binding.homeTopAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.sign_out -> {
                    viewModel.signOut()
                    return@setOnMenuItemClickListener true
                }
                else ->{
                    return@setOnMenuItemClickListener false
                }
            }
        }

    }


    private fun observeViewModel() {
        viewModel.authenticatedUser.observe(viewLifecycleOwner) {
            it?.let {
                binding.user = it
            }
            viewModel.authenticatedUser.value?.uuid?.let {
                viewModel.getOwnedFixRequests(it)
            }
        }

        viewModel.ownedFixRequest.observe(viewLifecycleOwner) {
            fixRequestsList = it
            binding.userFixRequestsList.layoutManager = GridLayoutManager(context, CARDS_IN_ROW, RecyclerView.VERTICAL, false)
            val adapter = FixRequestCardRecyclerViewAdapter(fixRequestsList)
            adapter.notifyDataSetChanged()
            binding.userFixRequestsList.adapter = adapter
            binding.userFixRequestsList.addItemDecoration(GridItemDecoration(this.requireContext(), R.dimen.padding))

        }
    }

}