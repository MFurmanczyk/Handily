package com.handily.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.handily.databinding.FragmentLoginBinding
import com.handily.viewmodel.LoginViewModel


class LoginFragment : Fragment(), LoginViewModel.LoginResultListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setLoginResultListener(this)

        binding.loginButton.setOnClickListener {
            viewModel.signInWithEmailAndPassword(
                binding.loginEmail.editText?.text.toString(), //email address
                binding.loginPassword.editText?.text.toString())
        }

        binding.googleSignInButton.setOnClickListener {
            //TODO: Not yet implemented
            Toast.makeText(context, "Not yet implemented.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess() {
        val intent = Intent(requireContext(), LandingActivity::class.java)
        startActivity(intent)
    }

    override fun onUserNotExists() {
        Snackbar.make(binding.root, "You have to create account first.", Snackbar.LENGTH_SHORT).show()
    }

    override fun onWrongPassword() {
        binding.loginPassword.error = "Wrong password!"
    }

    override fun onEmptyCredentials() {
        Snackbar.make(binding.root, "You have to enter email and password.", Snackbar.LENGTH_SHORT).show()
    }

}