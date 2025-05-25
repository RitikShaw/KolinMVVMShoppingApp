package com.ritikshaw.kolinmvvm.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.ritikshaw.kolinmvvm.activity.model.ItemData
import com.ritikshaw.kolinmvvm.databinding.LayoutDashboardRvItemBinding

class RecommendationAdapter() : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    private val diff_callback = object : DiffUtil.ItemCallback<ItemData>(){
        override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,diff_callback)

    fun submitList(list: List<ItemData>) = differ.submitList(list)

    class ViewHolder(val binding : LayoutDashboardRvItemBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view= LayoutDashboardRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = differ.currentList[position]

        holder.binding.apply {
            tvName.text = data.title
            tvPrice.text = "₹${data.price}"
        }
        Glide.with(holder.itemView.context)
            .load(data.picUrl[0])
            .transform(CenterInside())
            .into(holder.binding.imgProductIcon)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}