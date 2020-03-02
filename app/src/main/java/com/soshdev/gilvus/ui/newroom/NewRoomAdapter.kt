package com.soshdev.gilvus.ui.newroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.soshdev.gilvus.R
import com.soshdev.gilvus.data.models.Contact
import com.soshdev.gilvus.databinding.ItemContactBinding
import com.soshdev.gilvus.databinding.ItemContactDisabledBinding
import com.soshdev.gilvus.ui.base.BaseRecyclerViewAdapter
import com.soshdev.gilvus.ui.base.BaseViewHolder
import com.soshdev.gilvus.util.begone
import com.soshdev.gilvus.util.showUp

@Suppress("PrivatePropertyName")
class NewRoomAdapter(private val userClicked: (contact: Contact?) -> Unit) :
    BaseRecyclerViewAdapter<Contact, BaseViewHolder<ViewBinding>>() {

    private val TYPE_CONTACT_AVAILABLE = 1
    private val TYPE_CONTACT_UNAVAILABLE = 2
    private val selectedContacts = ArrayList<Contact>()

    override fun createHolder(
        inflater: LayoutInflater,
        root: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return if (viewType == TYPE_CONTACT_AVAILABLE)
            BaseViewHolder(ItemContactBinding.inflate(inflater, root, false))
        else
            BaseViewHolder(ItemContactDisabledBinding.inflate(inflater, root, false))
    }

    override fun bindHolder(
        item: Contact,
        holder: BaseViewHolder<ViewBinding>,
        position: Int
    ) =
        holder.binding.run {
            when (this) {
                is ItemContactBinding -> {
                    txName.text = item.name
                    item.phone?.let { txPhone.text = it }
                    layoutRoot.setOnClickListener {
                        if (item.phone == null) {
                            Snackbar.make(
                                root,
                                R.string.cant_add_user_wo_phone,
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                            return@setOnClickListener
                        }

                        if (selectedContacts.contains(item)) {
                            selectedContacts.remove(item)
                            imgChecked.begone()
                        } else {
                            selectedContacts.add(item)
                            imgChecked.showUp()
                        }

                        userClicked.invoke(item)
                    }
                }
                is ItemContactDisabledBinding -> {
                    txName.text = item.name
                    item.phone?.let { txPhone.text = it }
                    layoutRoot.setOnClickListener {
                        if (item.phone == null) {
                            Snackbar.make(
                                root,
                                R.string.cant_add_user_wo_phone,
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                            return@setOnClickListener
                        }

                        userClicked.invoke(null)
                    }
                }
            }
        }

    override fun getItemViewType(position: Int): Int =
        if (items[position].registered)
            TYPE_CONTACT_AVAILABLE
        else
            TYPE_CONTACT_UNAVAILABLE

}