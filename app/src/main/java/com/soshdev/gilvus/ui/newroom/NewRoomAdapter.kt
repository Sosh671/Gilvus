package com.soshdev.gilvus.ui.newroom

import android.view.LayoutInflater
import android.view.ViewGroup
import com.soshdev.gilvus.data.pojos.Contact
import com.soshdev.gilvus.databinding.ItemContactBinding
import com.soshdev.gilvus.ui.base.BaseRecyclerViewAdapter
import com.soshdev.gilvus.ui.base.BaseViewHolder
import com.soshdev.gilvus.util.begone
import com.soshdev.gilvus.util.showUp

class NewRoomAdapter(private val userClicked: (title: String) -> Unit) :
    BaseRecyclerViewAdapter<Contact, BaseViewHolder<ItemContactBinding>>() {

    private val selectedContacts = ArrayList<Contact>()

    override fun createHolder(
        inflater: LayoutInflater,
        root: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemContactBinding> {
        return BaseViewHolder(ItemContactBinding.inflate(inflater, root, false))
    }

    override fun bindHolder(item: Contact, holder: BaseViewHolder<ItemContactBinding>, position: Int) =
        holder.binding.run {
            txName.text = item.name
            item.phone?.let { txPhone.text = it}
            layoutRoot.setOnClickListener {
                if (selectedContacts.contains(item)) {
                    selectedContacts.remove(item)
                    imgChecked.begone()
                } else {
                    selectedContacts.add(item)
                    imgChecked.showUp()
                }

                val title = generateTitle()
                userClicked.invoke(title)
            }
        }

    private fun generateTitle(): String {
        val builder = StringBuilder()
        for (i in 0 until selectedContacts.size) {
            if (selectedContacts.size - i != 1)
                builder.append("${selectedContacts[i].name}, ")
            else
                builder.append(selectedContacts[i].name)
        }
        return builder.toString()
    }
}