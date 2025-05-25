package com.ritikshaw.kolinmvvm.activity.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.ritikshaw.kolinmvvm.R
import com.ritikshaw.kolinmvvm.activity.model.BrandModel
import com.ritikshaw.kolinmvvm.databinding.LayoutBrandlistRvItemBinding
import androidx.core.view.isGone


class BrandAdapter():RecyclerView.Adapter<BrandAdapter.ViewHolder>() {

    private var lastSelectedDataPos : Int ?=null
    private var selectedDataPos : Int ?=null
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
        val item = differ.currentList[holder.adapterPosition]

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .transform(CenterInside())
            .into(holder.binding.imgProductIcon)

        holder.binding.root.setOnClickListener {
            if (selectedDataPos==null){
                selectedDataPos = holder.adapterPosition
                lastSelectedDataPos = holder.adapterPosition
                notifyItemChanged(holder.adapterPosition)
            }
            else{
                selectedDataPos = holder.adapterPosition
                if (lastSelectedDataPos==selectedDataPos){
                    selectedDataPos = null
                    notifyItemChanged(holder.adapterPosition)
                }
                else{
                    notifyItemChanged(lastSelectedDataPos!!)
                    lastSelectedDataPos = holder.adapterPosition
                    notifyItemChanged(holder.adapterPosition)
                }
            }
        }

        holder.binding.apply {
            tvName.gravity = Gravity.CENTER
            tvName.text = item.title
            if (selectedDataPos!=null && selectedDataPos==holder.adapterPosition){
                itemParent.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.purple))
                tvName.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                ImageViewCompat.setImageTintList(imgProductIcon,ContextCompat.getColorStateList(holder.itemView.context,R.color.white))
                tvName.visibility  = View.VISIBLE
            }
            else{
                itemParent.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
                tvName.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.black))
                tvName.visibility  = View.GONE
                ImageViewCompat.setImageTintList(imgProductIcon,ContextCompat.getColorStateList(holder.itemView.context,R.color.black))

            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}