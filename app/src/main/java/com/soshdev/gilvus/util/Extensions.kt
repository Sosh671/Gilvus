package com.soshdev.gilvus.util

import io.reactivex.Single
import timber.log.Timber


fun <T : Any> Single<T>.androidSubscribe() =
    this.subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())


fun <T: Any> List<T>.toArrayList() = this.toMutableList() as ArrayList


fun Array<String>.printContents(/*printMethod: (String) -> Unit*/) {
    for (string in this) {
//        printMethod.invoke(string)
        Timber.d(string)
    }
}