package com.ritikshaw.kolinmvvm.activity.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.ritikshaw.kolinmvvm.R
import com.ritikshaw.kolinmvvm.activity.model.BrandModel
import com.ritikshaw.kolinmvvm.databinding.LayoutBrandlistRvItemBinding


class BrandAdapter():RecyclerView.Adapter<BrandAdapter.ViewHolder>() {

    private var lastSelectedData : BrandModel?=null
    class ViewHolder(val binding: LayoutBrandlistRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    private val _diffCallback = object : DiffUtil.ItemCallback<BrandModel>(){
        override fun areItemsTheSame(oldItem: BrandModel, newItem: BrandModel): Boolean {
            return oldItem.picUrl == newItem.picUrl
        }

        override fun areContentsTheSame(oldItem: BrandModel, newItem: BrandModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,_diffCallback)

    fun submitList(list: List<BrandModel>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutBrandlistRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .transform(CenterInside())
            .into(holder.binding.imgProductIcon)

        holder.binding.root.setOnClickListener {
            lastSelectedData = item
        }

        holder.binding.apply {
            tvName.gravity = Gravity.CENTER
            tvName.text = item.title
            if (lastSelectedData!=null && item.picUrl == lastSelectedData!!.picUrl){
                itemParent.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.purple))
                tvName.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
            }
            else{
                itemParent.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                tvName.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.black))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}