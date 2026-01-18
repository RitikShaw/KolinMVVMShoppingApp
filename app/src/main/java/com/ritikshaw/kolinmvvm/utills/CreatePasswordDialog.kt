package com.ritikshaw.kolinmvvm.utills

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.ritikshaw.kolinmvvm.databinding.LayoutSetpasswordDialogBinding

class CreatePasswordDialog(
    context: Context,
    private val onPasswordCreated:(String)->Unit
): Dialog(context) {

    val binding = LayoutSetpasswordDialogBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.apply {
            btnSave.setOnClickListener {
                if (etNewPassword.text.toString().isBlank()){
                    etNewPassword.error = "Password can't be empty"
                    return@setOnClickListener
                }
                if (etConPassword.text.toString().isBlank()){
                    etNewPassword.error = "Password can't be empty"
                    return@setOnClickListener
                }
                if (etNewPassword.text.toString()!=etConPassword.text.toString()){
                    etConPassword.error = "Password doesn't match"
                    return@setOnClickListener
                }
                onPasswordCreated(etNewPassword.text.toString())
                dismiss()
            }
        }
    }
}