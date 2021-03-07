package com.handily.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavAction
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.handily.R
import com.handily.databinding.FragmentFixesBinding

class FixesFragment : Fragment() {

    private var _binding: FragmentFixesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFixesBinding.inflate(inflater, container, false)
        return binding.root
    }
}