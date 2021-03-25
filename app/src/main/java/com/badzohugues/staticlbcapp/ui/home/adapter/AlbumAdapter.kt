package com.badzohugues.staticlbcapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.databinding.ItemAlbumBinding

class AlbumAdapter(private var itemAlbumClick: ((item: AlbumItem) -> Unit) = { }) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    var albumItems: List<AlbumItem> = ArrayList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    fun itemClick(itemClick:(item: AlbumItem) -> Unit) = apply { this.itemAlbumClick = itemClick }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val albumItem = albumItems[position]

        holder.bind(albumItem)
        holder.thumbnailImv.load(albumItem.thumbnailUrl) {
            crossfade(true)
        }
        holder.itemView.setOnClickListener { itemAlbumClick.invoke(albumItem) }
    }

    override fun getItemCount(): Int = albumItems.size

    class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnailImv = binding.imvThumbnail
        fun bind(itemData: AlbumItem) {
            binding.albumItemData = itemData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AlbumViewHolder {
                return AlbumViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
                )
            }
        }
    }
}