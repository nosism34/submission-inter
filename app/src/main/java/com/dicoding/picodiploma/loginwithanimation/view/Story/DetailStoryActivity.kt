package com.dicoding.picodiploma.loginwithanimation.view.Story

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val bundle: Bundle?= intent.extras
        val name = bundle!!.getString("name")
        val imageId = bundle.getString("imageId")
        val desc = bundle.getString("desc")

        binding.apply {
            tvName.text = name.toString()
            tvDesc.text = desc.toString()
            Glide.with(this@DetailStoryActivity)
                .load(imageId)
                .centerCrop()
                .into(ivPhoto)
        }
    }
}
