package com.ritikshaw.kolinmvvm.activity.authentication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ritikshaw.kolinmvvm.activity.authentication.AuthenticationActivity
import com.ritikshaw.kolinmvvm.activity.dashboard.DashboardActivity
import com.ritikshaw.kolinmvvm.activity.viewModel.AuthenticationViewModel
import com.ritikshaw.kolinmvvm.activity.viewModel.SharedPreferenceViewModel
import com.ritikshaw.kolinmvvm.databinding.FragmentLoginBinding
import com.ritikshaw.kolinmvvm.utills.AuthState
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel : AuthenticationViewModel by viewModels()
    private val sharedPreferenceViewModel : SharedPreferenceViewModel by viewModels()
    private lateinit var credentialManager : CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        credentialManager = CredentialManager.create(requireContext())

        binding.imgGoogleSignUp.setOnClickListener {
            signInWithGoogleFlow()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            authenticationViewModel.loginWithEmail(email,password)
        }
        loginWithObserver()
    }

    private fun loginWithObserver() {
        lifecycleScope.launch {
            authenticationViewModel.authStateLogin.collect { state ->
                when(state){
                    is AuthState.Loading -> {
                        // Show a progress bar or loading indicator
                        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    }
                    is AuthState.Success -> {
                        // Navigate to the main part of your app
                        sharedPreferenceViewModel.saveUserData(state.userData)
                        Toast.makeText(requireContext(), "Log-in Successful!", Toast.LENGTH_LONG).show()
                        val intent = Intent(requireActivity(), DashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    is AuthState.Error -> {
                        // Show an error message
                        Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun signInWithGoogleFlow() {
        if (activity is AuthenticationActivity){
            val googleCredentialsBuilder = (activity as AuthenticationActivity).buildGoogleSignInRequest()

            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(requireContext(),googleCredentialsBuilder)
                    val idToken = (activity as AuthenticationActivity).handleGoogleSignInResult(result)
                    authenticationViewModel.loginInWithGoogle(idToken)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}