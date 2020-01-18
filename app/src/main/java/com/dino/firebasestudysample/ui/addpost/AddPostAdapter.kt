package com.dino.firebasestudysample.ui.addpost

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dino.firebasestudysample.R
import kotlinx.android.synthetic.main.item_add_post_image.view.*

class AddPostAdapter : RecyclerView.Adapter<AddPostAdapter.ViewHolder>() {

    private val items = mutableListOf<Uri>()

    fun resetAll(newItems: List<Uri>) {
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
            .inflate(R.layout.item_add_post_image, parent, false)
    ) {

        private val ivImage = itemView.iv_image

        fun bind(uri: Uri) {
            Glide.with(ivImage)
                .load(uri)
                .into(ivImage)
        }
    }
}