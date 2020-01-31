package com.soshdev.gilvus.ui.newroom

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.database.getIntOrNull
import com.soshdev.gilvus.R
import com.soshdev.gilvus.databinding.FragmentNewroomBinding
import com.soshdev.gilvus.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class NewRoomFragment : BaseFragment() {

    private lateinit var binding: FragmentNewroomBinding
    private val vm: NewRoomViewModel by viewModel()
    private val REQUEST_READ_CONTACTS = 22

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar, R.string.home)

        getContactList()
    }

    private fun getContactList() {
        if (!checkPermission()) {
            requestPermission()
        } else {
            val cr = context?.contentResolver
            val contactCursor: Cursor? = cr?.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
                ),
                null,
                null,
                null
            )
            if ((contactCursor?.count ?: 0) > 0) {
                while (contactCursor != null && contactCursor.moveToNext()) {
                    val id: String =
                        contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name: String =
                        contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    Timber.d("Name: $name")

                    if (contactCursor.getIntOrNull(
                            contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                        ) ?: 0 > 0
                    ) {
                        val phoneCursor: Cursor? = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )

                        while (phoneCursor?.moveToNext() == true) {
                            val phoneNo: String =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            
                            Timber.d("Phone Number: $phoneNo")
                        }
                        phoneCursor?.close()
                    } else {
                        Timber.d("user has no phone number")
                    }
                }
            }
            contactCursor?.close()
        }
    }

    private fun checkPermission() = ActivityCompat.checkSelfPermission(
        context!!,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.READ_CONTACTS
            )
        ) {
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        } else {
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_READ_CONTACTS -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContactList()
                } else { // permission denied,Disable the
// functionality that depends on this permission.
                    Timber.d("no permission")
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}