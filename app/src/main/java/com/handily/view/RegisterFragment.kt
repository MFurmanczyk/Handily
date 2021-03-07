package com.handily.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.handily.databinding.FragmentRegisterBinding
import com.handily.viewmodel.LoginViewModel

class RegisterFragment : Fragment(), LoginViewModel.RegisterResultListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setRegisterResultListener(this)

        binding.policyButton.isChecked = false

        binding.registerButton.setOnClickListener {
            if (binding.policyButton.isChecked) {
                viewModel.registerUserWithEmailAndPassword(
                    binding.registerEmail.editText?.text.toString(), //email address
                    binding.registerPassword.editText?.text.toString(), //password
                    binding.registerPassword2.editText?.text.toString() //repeated password
                )
            } else {
                Snackbar.make(binding.root, "You have to accept privacy policy first.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(uuid: String) {
        val intent = Intent(requireActivity(), UserDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onEmailMalformed() {
        binding.registerEmail.error = "Enter correct email address."
    }

    override fun onPasswordTooShort() {
        binding.registerPassword.error = "Password must have at least 8 characters."
    }

    override fun onPasswordNotMatched() {
        binding.registerPassword.error = "Passwords don't match."
    }

    override fun onUserAlreadyExists() {
        Snackbar.make(binding.root, "Account with this email already exists. You can log in.", Snackbar.LENGTH_SHORT).show()
    }
}