package com.handily.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.handily.R
import com.handily.databinding.FragmentUserDetailsBinding
import com.handily.model.User
import com.handily.viewmodel.UserDetailsViewModel


class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailsViewModel by activityViewModels()

    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        observeViewModel()

        viewModel.setUser(firebaseAuth.currentUser?.uid.toString(),
            requireActivity() is UserDetailsActivity)
    }

    private fun setupToolbar() {
        binding.detailsTopAppBar.inflateMenu(R.menu.user_details_menu)

        binding.detailsTopAppBar.setOnMenuItemClickListener {

            //TODO: Handling fragment menu in other activities
            when (it?.itemId) {
                R.id.user_details_save -> {
                    if (!binding.userDetailsGivenName.editText?.text.isNullOrEmpty() &&
                        !binding.userDetailsFamilyName.editText?.text.isNullOrEmpty()
                    ) {

                        val user = User.Builder(binding.user?.uuid.toString(),
                            binding.user?.email.toString())
                            .givenName(binding.userDetailsGivenName.editText?.text.toString())
                            .familyName(binding.userDetailsFamilyName.editText?.text.toString())

                        if (!binding.userDetailsCountryCode.editText?.text.isNullOrEmpty() &&
                            !binding.userDetailsPhoneNumber.editText?.text.isNullOrEmpty()
                        ) {
                            user.countryPhoneCode(binding.userDetailsCountryCode.editText?.text.toString())
                                .phoneNumber(binding.userDetailsPhoneNumber.editText?.text.toString())
                        }

                        viewModel.saveUser(user.build(), requireActivity() is UserDetailsActivity)

                        if (activity is UserDetailsActivity) {
                            val intent = Intent(requireActivity(), LandingActivity::class.java)
                            startActivity(intent)
                        } else {
                            requireActivity().onBackPressed()
                        }
                    } else {
                        Snackbar.make(binding.root,
                            "Name and surname are mandatory",
                            Snackbar.LENGTH_SHORT).show()
                    }
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.authenticatedUser.observe(viewLifecycleOwner, {
            binding.user = it
        })
    }
}