package com.dicoding.picodiploma.loginwithanimation.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.Story.DetailStoryActivity

class Adapter(private val storiesList: List<ListStoryItem>): RecyclerView.Adapter <Adapter.MyViewHolder>() {
    class MyViewHolder(val binding: ItemStoryBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return storiesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.progressIndicator.visibility = View.VISIBLE
        val currentItem = storiesList[position]
        holder.binding.apply {
            tvName.text = currentItem.name
            tvDesc.text = currentItem.description
            Glide.with(card)
                .load(currentItem.photoUrl)
                .centerCrop()
                .into(ivPhoto)


            card.setOnClickListener {
                val intent = Intent(card.context, DetailStoryActivity::class.java)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        card.context as Activity,
                        androidx.core.util.Pair(ivPhoto, "image"),
                        androidx.core.util.Pair(tvName, "title"),
                        androidx.core.util.Pair(tvDesc, "desc"),
                    )
                intent.putExtra("name", currentItem.name!!)
                intent.putExtra("imageId", currentItem.photoUrl!!)
                intent.putExtra("desc", currentItem.description!!)
                card.context.startActivity(intent,optionsCompat.toBundle())

            }

        }

    }

}