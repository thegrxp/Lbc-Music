package com.ainrom.lbcmusic.view.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

@Suppress("UNCHECKED_CAST")
@BindingAdapter("data")
fun <T, V> setRecyclerViewData(recyclerView: RecyclerView, data: PagedList<T>?) {
    (recyclerView.adapter as DataBindingAdapter<T, V>).submitList(data)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (imageUrl != null)
        Picasso.get()
            .load(imageUrl)
            .fit()
            .centerCrop()
            .into(view)
}





