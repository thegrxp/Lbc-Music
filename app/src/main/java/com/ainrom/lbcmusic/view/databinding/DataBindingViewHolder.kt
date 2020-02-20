package com.ainrom.lbcmusic.view.databinding

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

class DataBindingViewHolder<T, V>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, viewModel: V) {
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }
}