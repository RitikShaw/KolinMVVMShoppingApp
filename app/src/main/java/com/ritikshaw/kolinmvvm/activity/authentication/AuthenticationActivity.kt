package com.ritikshaw.kolinmvvm.activity.authentication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.ritikshaw.kolinmvvm.R
import com.ritikshaw.kolinmvvm.activity.authentication.fragments.LoginFragment
import com.ritikshaw.kolinmvvm.activity.authentication.fragments.SignupFragment
import com.ritikshaw.kolinmvvm.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    lateinit var authenticationBinding : ActivityAuthenticationBinding
    private lateinit var fragmentManager : FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationBinding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(authenticationBinding.root)
        enableEdgeToEdge()

        goToFragment(SignupFragment())
        authenticationBinding.let { binding->
            binding.tvLoginSignUpGuide.setOnClickListener {
                val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                if (fragment != null) {
                    if (fragment is SignupFragment){
                        binding.tvSignUpLoginLabel.text = "Don't have an account? "
                        binding.tvLoginSignUpGuide.text = "Sign Up"
                        goToFragment(LoginFragment())
                    }
                    else{
                        binding.tvSignUpLoginLabel.text = "Already have an account? "
                        binding.tvLoginSignUpGuide.text = "Login"
                        goToFragment(SignupFragment())
                    }
                }
                else{
                    goToFragment(SignupFragment())
                }
            }
        }

    }

    private fun goToFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main,fragment).commit()
    }

    fun buildGoogleSignInRequest(): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(getString(com.ritikshaw.kolinmvvm.R.string.web_api_client))
                    .build()
            )
            .build()
    }

    fun handleGoogleSignInResult(result : GetCredentialResponse) : String{
        val credentials = result.credential
        val googleIdTokenCrendential = GoogleIdTokenCredential.createFrom(credentials.data)
        val idToken = googleIdTokenCrendential.idToken

        return idToken
    }
}