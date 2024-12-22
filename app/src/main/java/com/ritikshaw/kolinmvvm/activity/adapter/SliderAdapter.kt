package com.ritikshaw.kolinmvvm.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.ritikshaw.kolinmvvm.R
import com.ritikshaw.kolinmvvm.activity.model.SliderModel

class SliderAdapter(private val viewPager : ViewPager2):RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSlider : ImageView = itemView.findViewById(R.id.imgSlider)

        fun renderImage(imgData : SliderModel,context: Context){
            Glide.with(context)
                .load(imgData.url)
                .transform(CenterInside())
                .into(imgSlider)
        }
    }

    private val _diffCallback = object : DiffUtil.ItemCallback<SliderModel>(){
        override fun areItemsTheSame(oldItem: SliderModel, newItem: SliderModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: SliderModel, newItem: SliderModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,_diffCallback)

    fun submitList(list: List<SliderModel>) = differ.submitList(list)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.slider_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.renderImage(item,holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}