package com.ritikshaw.kolinmvvm.activity.intro

import android.content.Intent
import android.os.Bundle
import com.ritikshaw.kolinmvvm.activity.BaseActivity
import com.ritikshaw.kolinmvvm.activity.authentication.AuthenticationActivity
import com.ritikshaw.kolinmvvm.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    private lateinit var binding : ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            startActivity(Intent(this, AuthenticationActivity::class.java))
        }
    }
}