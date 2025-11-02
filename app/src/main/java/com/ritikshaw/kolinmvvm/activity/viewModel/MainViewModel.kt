package com.ritikshaw.kolinmvvm.activity.viewModel


import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ritikshaw.kolinmvvm.activity.model.BrandModel
import com.ritikshaw.kolinmvvm.activity.model.ItemData
import com.ritikshaw.kolinmvvm.activity.model.SliderModel

class MainViewModel() : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    val banner : LiveData<List<SliderModel>> = _banner

    private val _brand = MutableLiveData<List<BrandModel>>()
    val brand : LiveData<List<BrandModel>> = _brand

    private val _item = MutableLiveData<List<ItemData>>()
    val item : LiveData<List<ItemData>> = _item

    fun loadBanners(){
        val bannerRef = firebaseDatabase.getReference("banner")
        bannerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bannerList = mutableListOf<SliderModel>()
                for (bannerSnapshot in snapshot.children) {
                    val list = bannerSnapshot.getValue(SliderModel::class.java)
                    if (list!=null){
                        bannerList.add(list)
                        e("bannerSnapshot","$list")
                    }
                }

                _banner.value = bannerList
            }

            override fun onCancelled(error: DatabaseError) {
                e("error",error.toString())
            }
        })
    }

    fun loadBrands(){
        val bannerRef = firebaseDatabase.getReference("brands")
        bannerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val brandList = mutableListOf<BrandModel>()
                for (brandSnapshot in snapshot.children) {
                    val list = brandSnapshot.getValue(BrandModel::class.java)
                    if (list!=null){
                        brandList.add(list)
                        e("brandSnapshot","$list")
                    }
                }

                _brand.value = brandList
            }

            override fun onCancelled(error: DatabaseError) {
                e("error",error.toString())
            }
        })
    }

    fun loadRecommendations(){
        val categoryRef = firebaseDatabase.getReference("categories")
        val recommendation = categoryRef.child("shoe")
        recommendation.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = mutableListOf<ItemData>()
                for (brandSnapshot in snapshot.children) {
                    val list = brandSnapshot.getValue(ItemData::class.java)
                    if (list!=null){
                        itemList.add(list)
                        e("ItemsSnapshot","$list")
                    }
                }

                _item.value = itemList
            }

            override fun onCancelled(error: DatabaseError) {
                e("error",error.toString())
            }
        })
    }
}