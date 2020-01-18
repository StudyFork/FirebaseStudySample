package com.dino.firebasestudysample.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dino.firebasestudysample.R
import com.dino.firebasestudysample.ui.addpost.Post
import com.dino.firebasestudysample.extension.toDateTime
import kotlinx.android.synthetic.main.item_profile_post.view.*

class ProfilePostAdapter : RecyclerView.Adapter<ProfilePostAdapter.ViewHolder>() {

    private val items = mutableListOf<Post>()

    fun resetAll(newItems: List<Post>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent)

    override fun getItemCount() =
        items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])


    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_post, parent, false)
    ) {

        private val ivImage = itemView.iv_image
        private val tvDate = itemView.tv_date
        private val tvTitle = itemView.tv_title
        private val tvContent = itemView.tv_content

        fun bind(item: Post) {
            Glide.with(ivImage)
                .load(item.uriList[0])
                .into(ivImage)

            tvDate.text = item.date.toDateTime()
            tvTitle.text = item.title
            tvContent.text = item.content
        }

    }
}