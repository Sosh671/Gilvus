package com.soshdev.gilvus.data.models

data class BaseResponse<T> (
    val status: Boolean,
    val request: String,
    val errorMessage: String?,
    val data: T?
)