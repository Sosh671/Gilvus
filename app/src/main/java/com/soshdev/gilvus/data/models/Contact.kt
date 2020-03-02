package com.soshdev.gilvus.data.models

import android.net.Uri

data class Contact(
    val name: String,
    val phone: String?,
    val imagePath: Uri?,
    val registered: Boolean
)

