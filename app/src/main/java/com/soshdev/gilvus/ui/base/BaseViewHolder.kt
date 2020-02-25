package com.soshdev.gilvus.ui.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder<BINDING : ViewBinding>(
    val binding: BINDING
) : RecyclerView.ViewHolder(binding.root)