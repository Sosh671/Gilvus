package com.soshdev.gilvus.ui.roomslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.soshdev.gilvus.data.db.models.Room
import com.soshdev.gilvus.databinding.ItemRoomBinding
import com.soshdev.gilvus.ui.base.BaseRecyclerViewAdapter
import com.soshdev.gilvus.ui.base.BaseViewHolder

class RoomsListAdapter(private val userClicked: (userId: Long?) -> Unit) :
    BaseRecyclerViewAdapter<Room, BaseViewHolder<ItemRoomBinding>>() {

    override fun createHolder(
        inflater: LayoutInflater,
        root: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemRoomBinding> {
        return BaseViewHolder(ItemRoomBinding.inflate(inflater, root, false))
    }

    override fun bindHolder(item: Room, holder: BaseViewHolder<ItemRoomBinding>, position: Int) =
        holder.binding.run {
            txTitle.text = item.name
            layoutRoot.setOnClickListener { userClicked.invoke(item.id) }
        }

}