package com.handily.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.handily.databinding.FragmentMakeOfferBinding

class MakeOfferFragment : Fragment() {

    private var _binding: FragmentMakeOfferBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMakeOfferBinding.inflate(inflater, container, false)
        return binding.root
    }
}