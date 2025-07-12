package com.ritikshaw.kolinmvvm.activity.fragment

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ritikshaw.kolinmvvm.activity.adapter.ItemDetailsImageAdapter
import com.ritikshaw.kolinmvvm.activity.model.ItemData
import com.ritikshaw.kolinmvvm.databinding.FragmentItemDetailsSheetListDialogItemBinding


class ItemDetailsSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentItemDetailsSheetListDialogItemBinding? = null
    private lateinit var imageAdapter : ItemDetailsImageAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailsSheetListDialogItemBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageAdapter = ItemDetailsImageAdapter()
        val item = arguments?.getSerializable("item") as ItemData
        binding.tvProdName.text = item.title
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            bottomSheet.let {
                val bottomSheetDialog = dialogInterface as BottomSheetDialog
                val bottomSheet =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    // Make it expanded and non-collapsible
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.skipCollapsed = true

                    val displayMatrics = resources.displayMetrics
                    val height = displayMatrics.heightPixels
                    val maxHeight = (height * 0.90).toInt()

                    // Set full height
                    it.layoutParams.height = maxHeight
                }
            }
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        // Make dialog full screen width too
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val displayMatrics = resources.displayMetrics
            val height = displayMatrics.heightPixels
            val maxHeight = (height * 0.90).toInt()
            bottomSheet?.layoutParams?.height = maxHeight
            val window = it.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ItemDetailsSheetFragment"
        fun newInstance(bundle: Bundle?): ItemDetailsSheetFragment {
            val fragment = ItemDetailsSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}