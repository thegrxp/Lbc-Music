package com.ainrom.lbcmusic.view.databinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class DataBindingAdapter<T, V>(diffCallback: DiffUtil.ItemCallback<T>, open val viewModel: V) :
    PagedListAdapter<T, DataBindingViewHolder<T, V>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T, V> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T, V>, position: Int) {
        getItem(position)?.let { holder.bind(it, viewModel) }
    }

    abstract override fun getItemViewType(position: Int): Int
}