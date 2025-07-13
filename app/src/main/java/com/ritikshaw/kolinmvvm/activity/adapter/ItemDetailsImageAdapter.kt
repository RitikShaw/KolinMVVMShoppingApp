package com.ritikshaw.kolinmvvm.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ritikshaw.kolinmvvm.databinding.ItemdetailsSliderimagesBinding

class ItemDetailsImageAdapter(): RecyclerView.Adapter<ItemDetailsImageAdapter.ViewHolder>() {

    private val _diffCallBAck = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }

    val diffUtill = AsyncListDiffer(this,_diffCallBAck)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ItemdetailsSliderimagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
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

    class ViewHolder(binding: ItemdetailsSliderimagesBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}