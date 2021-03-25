package com.badzohugues.staticlbcapp.ui.albumdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.badzohugues.staticlbcapp.R
import com.badzohugues.staticlbcapp.data.domain.AlbumItem
import com.badzohugues.staticlbcapp.databinding.ItemGridAlbumBinding

class AlbumDetailsAdapter(private var itemAlbumClick: ((item: AlbumItem) -> Unit) = { }) : RecyclerView.Adapter<AlbumDetailsAdapter.AlbumViewHolder>() {
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
        holder.thumbnailImv.load(albumItem.url) {
            crossfade(true)
            placeholder(R.drawable.shape_square_placeholder)
            error(R.drawable.shape_square_placeholder)
        }
        holder.itemView.setOnClickListener { itemAlbumClick.invoke(albumItem) }
    }

    override fun getItemCount(): Int = albumItems.size

    class AlbumViewHolder(private val binding: ItemGridAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnailImv = binding.imvGridItem
        fun bind(itemData: AlbumItem) {
            binding.albumItemData = itemData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AlbumViewHolder {
                return AlbumViewHolder(
                    ItemGridAlbumBinding.inflate(
                        LayoutInflater.from(parent.context),
                    parent,
                    false)
                )
            }
        }
    }
}