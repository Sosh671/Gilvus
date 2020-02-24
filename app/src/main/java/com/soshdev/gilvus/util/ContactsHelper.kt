package com.soshdev.gilvus.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.database.getIntOrNull
import com.soshdev.gilvus.data.models.Contact
import timber.log.Timber

class ContactsHelper(private val context: Context) {

    fun getContacts(): List<Contact> {
        val cr = context.contentResolver
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

        val array = ArrayList<Contact>()

        if ((contactCursor?.count ?: 0) > 0) {
            while (contactCursor != null && contactCursor.moveToNext()) {

                val id: String =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
                val contactName =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val contactPhoneNumber = getPhoneNumber(cr, contactCursor, id)
                val contactPhotoUri = getPhoto(cr, id)

                Timber.d("Name: $contactName")
                Timber.d("Phone Number: $contactPhoneNumber")
                Timber.d("Photo uri: $contactPhotoUri")

                array.add(Contact(contactName, contactPhoneNumber, contactPhotoUri))
            }

        }
        contactCursor?.close()

        return array
    }

    private fun getPhoneNumber(cr: ContentResolver, contactCursor: Cursor, id: String): String? {
        if (contactCursor.getIntOrNull(
                contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
            ) ?: 0 > 0
        ) {
            val phoneCursor: Cursor? = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                arrayOf(id),
                null
            )

            if (phoneCursor?.moveToNext() == true) {
                return phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            }
            phoneCursor?.close()
        }
        return null
    }

    private fun getPhoto(cr: ContentResolver, id: String): Uri? {
        val photoCursor = cr.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = $id AND ${ContactsContract.Data.MIMETYPE} = '${ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}'",
            null,
            null
        )

        photoCursor?.let {
            if (photoCursor.moveToFirst()) {
                val person: Uri =
                    ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        id.toLong()
                    )
                return Uri.withAppendedPath(
                    person,
                    ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                )
            }
            photoCursor.close()
        }
        return null
    }
}