package com.soshdev.gilvus.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class BaseRecyclerViewAdapter<ITEM, VH: RecyclerView.ViewHolder>
    : RecyclerView.Adapter<VH>() {

    protected val items = ArrayList<ITEM>()

    abstract fun createHolder(inflater: LayoutInflater, root: ViewGroup, viewType: Int): VH

    abstract fun bindHolder(item: ITEM, holder: VH, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return createHolder(inflater, parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        bindHolder(item, holder, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun clear() {
        items.clear()
    }

    fun clearAndUpdate() {
        clear()
        notifyDataSetChanged()
    }

    fun add(item: ITEM) {
        items.add(item)
        notifyItemInserted(items.size)
    }

    fun add(list: List<ITEM>) {
        items.addAll(list)
        notifyItemRangeChanged(items.size - list.size, list.size)
    }
}