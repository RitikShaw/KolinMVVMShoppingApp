package com.ritikshaw.kolinmvvm.activity.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.ritikshaw.kolinmvvm.activity.BaseActivity
import com.ritikshaw.kolinmvvm.activity.authentication.AuthenticationActivity
import com.ritikshaw.kolinmvvm.activity.dashboard.DashboardActivity
import com.ritikshaw.kolinmvvm.activity.viewModel.SharedPreferenceViewModel
import com.ritikshaw.kolinmvvm.databinding.ActivityIntroBinding
import kotlin.getValue

class IntroActivity : BaseActivity() {

    private lateinit var binding : ActivityIntroBinding
    private val sharedPreferenceViewModel : SharedPreferenceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferenceViewModel.getUserData.observe(this) { userData ->

            if (userData == null || userData.userId.isEmpty()) {
                startActivity(Intent(this, AuthenticationActivity::class.java))

            }
            else{
                startActivity(Intent(this, DashboardActivity::class.java))
            }

        }

        /*binding.start.setOnClickListener {
            val userData = sharedPreferenceViewModel.getUserData.value

            if (userData!=null){
                startActivity(Intent(this, DashboardActivity::class.java))

            }
            else{
                startActivity(Intent(this, AuthenticationActivity::class.java))
            }
        }*/
    }
}