package com.ainrom.lbcmusic.view.albums

import androidx.recyclerview.widget.DiffUtil
import com.ainrom.lbcmusic.R
import com.ainrom.lbcmusic.data.local.album.AlbumEntity
import com.ainrom.lbcmusic.view.databinding.DataBindingAdapter

class AlbumsAdapter(override val viewModel: AlbumsListViewModel) :
    DataBindingAdapter<AlbumEntity, AlbumsListViewModel>(object : DiffUtil.ItemCallback<AlbumEntity>() {
        override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }, viewModel) {

    override fun getItemViewType(position: Int): Int = R.layout.item_album
}