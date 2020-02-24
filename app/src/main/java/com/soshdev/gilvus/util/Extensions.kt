package com.soshdev.gilvus.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.Single
import timber.log.Timber


fun <T : Any> Single<T>.androidSubscribe() =
    this.subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())


fun <T : Any> List<T>.toArrayList() = this.toMutableList() as ArrayList


fun Array<String>.printContents(/*printMethod: (String) -> Unit*/) {
    for (string in this) {
//        printMethod.invoke(string)
        Timber.d(string)
    }
}

fun IntArray.print() {
    Timber.d("size $size")
    for (i in 0 until size)
        Timber.d("${this[i]}")
}

fun View.begone() {
    visibility = GONE
}

fun View.showUp() {
    visibility = VISIBLE
}


fun TextInputEditText.showKeyboard(activity: Activity) {
    requestFocus()
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        this,
        InputMethodManager.SHOW_IMPLICIT
    )
}