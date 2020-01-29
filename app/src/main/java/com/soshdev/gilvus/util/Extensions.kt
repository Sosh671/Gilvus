package com.soshdev.gilvus.util

import io.reactivex.Single


fun <T : Any> Single<T>.androidSubscribe() =
    this.subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())


fun <T: Any> List<T>.toArrayList() = this.toMutableList() as ArrayList