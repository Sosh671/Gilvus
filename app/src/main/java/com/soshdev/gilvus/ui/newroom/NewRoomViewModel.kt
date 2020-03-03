package com.soshdev.gilvus.ui.newroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soshdev.gilvus.data.DbRepository
import com.soshdev.gilvus.data.db.models.User
import com.soshdev.gilvus.ui.base.BaseViewModel
import com.soshdev.gilvus.util.compareRawPhoneNumbers
import com.soshdev.gilvus.util.launchOnIO
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class NewRoomViewModel(private val dbRepository: DbRepository) : BaseViewModel() {

    private val allContacts = ArrayList<User>()
    private val selectedContacts = ArrayList<User>()

    private val _roomTitle = MutableLiveData<String>()
    val roomTitle: LiveData<String> = _roomTitle

    private val _validatedContacts = MutableLiveData<List<User>>()
    val validatedContacts: LiveData<List<User>> = _validatedContacts

    private val _createRoomStatus = MutableLiveData<Boolean>()
    val createRoomStatus: LiveData<Boolean> = _createRoomStatus

    init {
        disposables += networkRepository.checkContactsSubject
            .subscribeBy(
                onNext = {
                    if (it.status)
                        it.data?.let { contacts ->
                            for (contact in allContacts) {
                                val index = contacts.indexOfFirst { idAndPhone ->
                                    contact.phone?.compareRawPhoneNumbers(idAndPhone.phone) ?: false
                                }
                                if (index >= 0) {
                                    contact.id = contacts[index].id
                                    contact.registered = true
                                }
                            }

                            _validatedContacts.postValue(allContacts)
                            // todo save to db
                        }

                    // todo else
                },
                onError = {
                    Timber.e(it)
                    //todo on error
                }
            )
        disposables += networkRepository.createRoomSubject
            .subscribeBy(
                onNext = {
                    _createRoomStatus.postValue(it.status)
                },
                onError = {
                    Timber.e(it)
                    //todo on error
                }
            )
    }

    fun checkMyContactsExist(token: String, contacts: List<User>) {
        allContacts.clear()
        allContacts.addAll(contacts)

        viewModelScope.launchOnIO {
            networkRepository.checkContacts(
                token,
                contacts.mapNotNull { c -> c.phone }.toTypedArray()
            )
        }
    }

    fun createRoom(token: String, title: String) {
        viewModelScope.launchOnIO {
            networkRepository.createRoom(
                token,
                title,
                selectedContacts.mapNotNull { it.id }.toTypedArray()
            )
        }
    }

    fun selectContact(contact: User) {
        if (selectedContacts.contains(contact))
            selectedContacts.remove(contact)
        else
            selectedContacts.add(contact)

        _roomTitle.value = generateTitle()
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