package com.soshdev.gilvus.ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<BINDING : ViewDataBinding>(
    val binding: BINDING
) : RecyclerView.ViewHolder(binding.root)