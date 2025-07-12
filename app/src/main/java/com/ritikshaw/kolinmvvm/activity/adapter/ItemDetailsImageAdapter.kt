package com.ritikshaw.kolinmvvm.activity.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ritikshaw.kolinmvvm.databinding.ItemdetailsSliderimagesBinding

class ItemDetailsListAdapter(): RecyclerView.Adapter<ItemDetailsListAdapter.ViewHolder>() {

    private val diff_utill = object : DiffUtil.ItemCallback<I>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ViewHolder(val binding: ItemdetailsSliderimagesBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}