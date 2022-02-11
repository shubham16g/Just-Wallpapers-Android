package com.shubhamgupta16.wallpaperapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.wallpaperapp.adapters.SingleImageAdapter
import com.shubhamgupta16.wallpaperapp.databinding.ActivityMainBinding
import com.shubhamgupta16.wallpaperapp.models.app.WallModel
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.utils.apply3DSwiper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }
    private lateinit var binding:ActivityMainBinding

    private val list = ArrayList<WallModel>()
    private var adapter :SingleImageAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadImagesListFromServer()
    }

    private fun loadImagesListFromServer(){
        val picsumService = ApiService.getInstance()
        CoroutineScope(Dispatchers.IO).launch {
            val response = picsumService.getWalls(perPage = 100)
            if (response.isSuccessful) {
                Log.d(TAG, "body: ${response.body()}")
                response.body()?.let {
                    list.addAll(it.data)
                    withContext(Dispatchers.Main) {
                        setupViewPager()
                    }
                }
            }
        }
    }

    private fun setupViewPager() {

        adapter = SingleImageAdapter(this, list){

        }
        binding.viewPager2.adapter = adapter
        binding.viewPager2.apply3DSwiper()
//        binding.viewPager2.offscreenPageLimit = 1
//        (binding.viewPager2.getChildAt(0)as RecyclerView).apply {
//            setItemViewCacheSize(50)
//            setHasFixedSize(true)
//        }
    }
}