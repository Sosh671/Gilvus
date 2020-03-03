package com.soshdev.gilvus.data.models

data class Status(
    val success: Boolean,
    val resId: Int? = null,
    val errorMsg: String? = null
)