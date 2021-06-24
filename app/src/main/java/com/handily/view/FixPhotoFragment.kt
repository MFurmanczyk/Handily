package com.handily.view

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.handily.databinding.FragmentFixPhotoBinding
import com.handily.util.getProgressDrawable
import com.handily.util.loadImage


class FixPhotoFragment(private val bitmapUrl: String) : Fragment() {

    private var _binding: FragmentFixPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFixPhotoBinding.inflate(inflater, container, false)
        binding.fixPhoto.loadImage(bitmapUrl, getProgressDrawable(binding.fixPhoto.context))
        return binding.root
    }


}