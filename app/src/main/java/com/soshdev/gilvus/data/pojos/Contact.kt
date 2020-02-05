package com.soshdev.gilvus.data.pojos

import android.net.Uri

data class Contact(
    val name: String,
    val phone: String?,
    val imagePath: Uri?
)

