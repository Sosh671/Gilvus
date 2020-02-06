package com.soshdev.gilvus.ui.newroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soshdev.gilvus.data.DbRepository
import com.soshdev.gilvus.data.db.models.Room
import com.soshdev.gilvus.data.pojos.Contact
import com.soshdev.gilvus.ui.base.BaseViewModel

class NewRoomViewModel(private val dbRepository: DbRepository) : BaseViewModel() {

    private val selectedContacts = ArrayList<Contact>()

    private val _roomTitle = MutableLiveData<String>()
    val roomTitle: LiveData<String> = _roomTitle

    fun selectContact(contact: Contact) {
        if (selectedContacts.contains(contact))
            selectedContacts.remove(contact)
        else
            selectedContacts.add(contact)

        _roomTitle.value = generateTitle()
    }

    fun addRoom() {
        val title = roomTitle.value ?: return
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