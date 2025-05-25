package com.ritikshaw.kolinmvvm.activity.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ritikshaw.kolinmvvm.activity.adapter.BrandAdapter
import com.ritikshaw.kolinmvvm.activity.adapter.RecommendationAdapter
import com.ritikshaw.kolinmvvm.activity.adapter.SliderAdapter
import com.ritikshaw.kolinmvvm.activity.model.BrandModel
import com.ritikshaw.kolinmvvm.activity.model.ItemData
import com.ritikshaw.kolinmvvm.activity.model.SliderModel
import com.ritikshaw.kolinmvvm.activity.viewModel.MainViewModel
import com.ritikshaw.kolinmvvm.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var adapter : SliderAdapter
    private lateinit var brandAdapter : BrandAdapter
    private lateinit var recommendationAdapter: RecommendationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initBrand()
        initRecommendations()
    }


    private fun initBanner() {
        binding.pbBanner.visibility = View.VISIBLE
        viewModel.banner.observe(this, Observer { items->
            banners(items)
            binding.pbBanner.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun initRecommendations() {
        binding.pbRecommended.visibility = View.VISIBLE
        viewModel.loadRecommendations()
        viewModel.item.observe(this, Observer { items->
            recommendedItems(items)
            binding.pbRecommended.visibility = View.GONE
        })
    }

    private fun recommendedItems(items: List<ItemData>) {
        recommendationAdapter= RecommendationAdapter()
        binding.rvRecommendedBrands.layoutManager = GridLayoutManager(this,2)
        binding.rvRecommendedBrands.adapter=recommendationAdapter
        recommendationAdapter.submitList(items)

    }



    private fun initBrand() {
        binding.pbOfficial.visibility = View.VISIBLE
        viewModel.brand.observe(this, Observer { items->
            brand(items)
            binding.pbOfficial.visibility = View.GONE
        })
        viewModel.loadBrands()
    }

    private fun banners(items: List<SliderModel>) {
        adapter=SliderAdapter(binding.viewPagerBanner)
        binding.viewPagerBanner.adapter=adapter
        binding.viewPagerBanner.clipToPadding = false
        binding.viewPagerBanner.clipChildren = false
        binding.viewPagerBanner.offscreenPageLimit = 3
        binding.viewPagerBanner.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        adapter.submitList(items)

        if (items.isNotEmpty()){
            binding.dotsIndicator.visibility = View.VISIBLE
            binding.dotsIndicator.attachTo(binding.viewPagerBanner)
        }else{
            binding.dotsIndicator.visibility = View.GONE
        }
    }

    private fun brand(items: List<BrandModel>) {
        brandAdapter= BrandAdapter()
        binding.rvOfficialBrands.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvOfficialBrands.adapter=brandAdapter
        brandAdapter.submitList(items)

    }
}