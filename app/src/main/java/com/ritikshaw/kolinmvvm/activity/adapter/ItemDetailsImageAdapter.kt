package com.ritikshaw.kolinmvvm.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
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
        val picUrl = diffUtill.currentList[position]
        Glide.with(holder.itemView.context)
            .load(picUrl)
            .transform(FitCenter())
            .into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return diffUtill.currentList.size
    }

    class ViewHolder(val binding: ItemdetailsSliderimagesBinding) : RecyclerView.ViewHolder(binding.root)
}