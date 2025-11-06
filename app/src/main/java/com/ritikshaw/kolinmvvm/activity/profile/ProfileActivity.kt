package com.ritikshaw.kolinmvvm.activity.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ritikshaw.kolinmvvm.activity.model.UserData
import com.ritikshaw.kolinmvvm.activity.viewModel.ProfileViewModel
import com.ritikshaw.kolinmvvm.activity.viewModel.SharedPreferenceViewModel
import com.ritikshaw.kolinmvvm.databinding.ActivityProfileBinding
import com.ritikshaw.kolinmvvm.databinding.BottomsheetProfileUpdateBinding
import com.ritikshaw.kolinmvvm.utills.AuthState
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private val binding : ActivityProfileBinding by lazy { ActivityProfileBinding.inflate(layoutInflater) }

    private val profileViewModel : ProfileViewModel by viewModels()
    private lateinit var sharedViewModel : SharedPreferenceViewModel
    private var userData = UserData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sharedViewModel = SharedPreferenceViewModel(application = application)


        sharedViewModel.getUserData.observe(this, Observer{ userData ->
            profileViewModel.getUserDataFromFirebase(userData.userId)
        })

        getUserData()

        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetBinding = BottomsheetProfileUpdateBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(android.R.color.transparent)
        }

        binding.apply {
            profileName.setOnClickListener {
                bottomSheetBinding.tilLabel.hint = "Name"
                bottomSheetBinding.etInput.setText( profileName.text)
                bottomSheetDialog.show()
            }

            tvEmail.setOnClickListener {
                bottomSheetBinding.tilLabel.hint = "Email"
                bottomSheetBinding.etInput.setText(tvEmail.text)
                bottomSheetDialog.show()
            }

            phone.setOnClickListener {
                bottomSheetBinding.tilLabel.hint = "Phone"
                bottomSheetBinding.etInput.setText(phone.text)
                bottomSheetDialog.show()
            }

        }

        bottomSheetBinding.updateProfile.setOnClickListener {
            if (bottomSheetBinding.etInput.text.toString().trim().isEmpty()){
                Toast.makeText(this,"Please Enter ${bottomSheetBinding.tilLabel.hint}",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            updateProfile(bottomSheetBinding.tilLabel.hint.toString(),bottomSheetBinding.etInput.text.toString())
            bottomSheetDialog.dismiss()
        }
    }

    // In ProfileActivity.kt

    private fun updateProfile(hint: String, updatedValue: String) {
        // 1. Get the CURRENT state directly from the ViewModel's StateFlow.
        val currentState = profileViewModel.profileState.value

        // 2. Only proceed if the state is Success and contains user data.
        if (currentState is AuthState.Success) {
            // 3. Create a shallow copy of the LATEST user data.
            val updatedUserData = currentState.userData.copy()

            // 4. Update the copied object based on the hint.
            when (hint) {
                "Name" -> {
                    updatedUserData.name = updatedValue
                }
                "Email" -> {
                    updatedUserData.email = updatedValue
                }
                "Phone" -> {
                    updatedUserData.phoneNumber = updatedValue
                }
            }

            // 5. Send this newly modified object to the ViewModel to be saved.
            profileViewModel.updateProfile(updatedUserData)

        } else {
            // This is a good practice to handle cases where data might not be loaded yet.
            Toast.makeText(this, "Could not get user data to perform update.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getUserData() {
        lifecycleScope.launch {
            profileViewModel.profileState.collect { profileState->
                when(profileState){
                    is AuthState.Loading-> {}
                    is AuthState.Success-> {
                        userData = profileState.userData
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