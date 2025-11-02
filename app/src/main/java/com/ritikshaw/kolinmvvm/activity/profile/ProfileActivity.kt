package com.ritikshaw.kolinmvvm.activity.profile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ritikshaw.kolinmvvm.activity.viewModel.ProfileViewModel
import com.ritikshaw.kolinmvvm.activity.viewModel.SharedPreferenceViewModel
import com.ritikshaw.kolinmvvm.databinding.ActivityProfileBinding
import com.ritikshaw.kolinmvvm.utills.AuthState
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private val binding : ActivityProfileBinding by lazy { ActivityProfileBinding.inflate(layoutInflater) }

    private val profileViewModel : ProfileViewModel by viewModels()
    private lateinit var sharedViewModel : SharedPreferenceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sharedViewModel = SharedPreferenceViewModel(application = application)


        sharedViewModel.getUserData.observe(this, Observer{ userData ->
            profileViewModel.getUserDataFromFirebase(userData.userId)
        })


        getUserData()
    }

    private fun getUserData() {
        lifecycleScope.launch {
            lifecycleScope.launch {
                profileViewModel.profileState.collect { profileState->
                    when(profileState){
                        is AuthState.Loading-> {}
                        is AuthState.Success-> {
                            binding.tvEmail.text = profileState.userData.email
                            binding.profileName.text = profileState.userData.name
                            binding.phone.text = profileState.userData.phoneNumber
                        }
                        is AuthState.Error -> {}
                        AuthState.Idle -> {}
                    }
                }
            }
        }
    }
}