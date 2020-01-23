package com.soshdev.gilvus.ui.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.soshdev.gilvus.data.models.Chat
import com.soshdev.gilvus.databinding.ItemUserBinding
import com.soshdev.gilvus.ui.base.BaseRecyclerViewAdapter
import com.soshdev.gilvus.ui.base.BaseViewHolder
import timber.log.Timber

class ChatListAdapter(private val userClicked: (userId: Int) -> Unit) :
    BaseRecyclerViewAdapter<Chat, BaseViewHolder<ItemUserBinding>>() {

    override fun createHolder(
        inflater: LayoutInflater,
        root: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemUserBinding> {
        return BaseViewHolder(ItemUserBinding.inflate(inflater, root, false))
    }

    override fun bindHolder(item: Chat, holder: BaseViewHolder<ItemUserBinding>, position: Int) =
        holder.binding.run {
            txUserName.text = item.name
            layoutRoot.setOnClickListener { Timber.d("click");userClicked.invoke(item.id) }
        }

}