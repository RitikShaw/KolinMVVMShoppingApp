package com.ritikshaw.kolinmvvm.activity.authentication.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ritikshaw.kolinmvvm.activity.authentication.AuthenticationActivity
import com.ritikshaw.kolinmvvm.activity.dashboard.DashboardActivity
import com.ritikshaw.kolinmvvm.activity.viewModel.AuthenticationViewModel
import com.ritikshaw.kolinmvvm.activity.viewModel.SharedPreferenceViewModel
import com.ritikshaw.kolinmvvm.databinding.FragmentSignupBinding
import com.ritikshaw.kolinmvvm.utills.AuthState
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {

    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel : AuthenticationViewModel by viewModels()
    private val sharedPreferenceViewModel : SharedPreferenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imgGoogleSignUp.setOnClickListener {
            signInWithGoogleFlow()
        }
        observeAuthState()
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            authenticationViewModel.authStateSignUp.collect { authState ->
                when (authState) {
                    is AuthState.Loading -> {
                        // Show a progress bar or loading indicator
                        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    }

                    is AuthState.Success -> {
                        sharedPreferenceViewModel.saveUserData(authState.userData)
                        Toast.makeText(requireContext(), "Sign-in Successful!", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(requireActivity(), DashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }

                    is AuthState.Error -> {
                        // Show an error message
                        Toast.makeText(
                            requireContext(),
                            "Error: ${authState.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit // Idle state
                }
            }
        }

    }

    private fun signInWithGoogleFlow() {
        val googleSingInRequest = if (activity is AuthenticationActivity){
            (activity as AuthenticationActivity).buildGoogleSignInRequest()
        }
        else{
            null
        }
        lifecycleScope.launch {
            try {
                if (googleSingInRequest!=null){
                    val credentialManager = CredentialManager.create(requireContext())
                    val result = credentialManager.getCredential(
                        context = requireContext(),
                        request = googleSingInRequest
                    )
                    d("GoogleSignIn", "Got credential response!")
                    val idToken = (activity as AuthenticationActivity).handleGoogleSignInResult(result)
                    authenticationViewModel.signInWithGoogle(idToken)
                }
            } catch (e: GetCredentialException) {
                // Handle errors like user cancellation
                e("GoogleSignIn", "GetCredentialException: ${e.message}")
            }
        }
    }






}