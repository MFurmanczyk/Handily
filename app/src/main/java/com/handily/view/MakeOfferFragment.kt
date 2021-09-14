package com.handily.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.handily.databinding.FragmentMakeOfferBinding
import com.handily.model.FixOffer
import com.handily.viewmodel.HandilyViewModel
import com.handily.viewmodel.OfferViewModel

class MakeOfferFragment(private val fixUuid: String) : Fragment() {

    private var _binding: FragmentMakeOfferBinding? = null
    private val binding get() = _binding!!

    protected val viewModel : OfferViewModel by activityViewModels()
    protected val userViewModel : HandilyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMakeOfferBinding.inflate(inflater, container, false)
        binding.button.setOnClickListener {
            var fixOffer = FixOffer.Builder(userViewModel.authenticatedUser.value?.uuid.toString(),
                fixUuid,
                binding.price.text.toString().toInt(),
                binding.currency.text.toString())
                .build()
            viewModel.addFixOffer(fixOffer)
            Toast.makeText(context, "Offer maked.", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}