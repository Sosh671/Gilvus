package com.soshdev.gilvus.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.soshdev.gilvus.data.db.models.Message
import com.soshdev.gilvus.databinding.ItemMessageReceivedBinding
import com.soshdev.gilvus.databinding.ItemMessageSentBinding
import com.soshdev.gilvus.ui.base.BaseRecyclerViewAdapter
import com.soshdev.gilvus.ui.base.BaseViewHolder

class ChatAdapter : BaseRecyclerViewAdapter<Message, BaseViewHolder<ViewBinding>>() {

    private val TYPE_MESSAGE_SENT = 1
    private val TYPE_MESSAGE_RECEIVED = 2

    override fun createHolder(
        inflater: LayoutInflater,
        root: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return if (viewType == TYPE_MESSAGE_SENT)
            BaseViewHolder(ItemMessageSentBinding.inflate(inflater, root, false))
        else
            BaseViewHolder(ItemMessageReceivedBinding.inflate(inflater, root, false))
    }

    override fun bindHolder(item: Message, holder: BaseViewHolder<ViewBinding>, position: Int) =
        holder.binding.let { binding ->
            when (binding ) {
                is ItemMessageReceivedBinding -> {
                    binding.txMessage.text = item.text
                }
                is ItemMessageSentBinding -> {
                    binding.txMessage.text = item.text
                }

            }
        }

    override fun getItemViewType(position: Int): Int {
        val switch = items[position].sentByCurrentUser
        return if (switch)
            TYPE_MESSAGE_SENT
        else
            TYPE_MESSAGE_RECEIVED
    }
}