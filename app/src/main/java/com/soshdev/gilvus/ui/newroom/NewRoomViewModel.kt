package com.soshdev.gilvus.ui.newroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.data.DbRepository
import com.soshdev.gilvus.data.models.Contact
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.launchOnIO
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class NewRoomViewModel(private val dbRepository: DbRepository) : BaseViewModel() {

    private val selectedContacts = ArrayList<Contact>()

    private val _roomTitle = MutableLiveData<String>()
    val roomTitle: LiveData<String> = _roomTitle

    init {
        disposables += networkRepository.checkContactsSubject
            .subscribeBy(
                onNext = {
                    if (it.status)
                        it.data?.let { contacts ->
//                            _rooms.postValue(rooms.toArrayList())
                            Timber.d("con $contacts")
                            // todo save to db
                        }

                    // todo else
                },
                onError = {
                    Timber.e(it)
                    //todo on error
                }
            )
    }

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

    fun checkMyContactsExist(token: String, contacts: Array<String>) {
        viewModelScope.launchOnIO {
            networkRepository.checkContacts(token, contacts)
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