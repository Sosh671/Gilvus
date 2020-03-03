package com.soshdev.gilvus.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.Single
import kotlinx.coroutines.*


fun <T : Any> Single<T>.androidSubscribe() =
    this.subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())

fun CoroutineScope.launchOnIO(
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, exception -> },
    block: suspend () -> Unit
): Job {
    return this.launch(Dispatchers.IO + exceptionHandler) {
        block.invoke()
    }
}

fun View.createSnackbar(
    resId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionText: Int? = null,
    actionListener: View.OnClickListener? = null
): Snackbar {
    val snack = Snackbar.make(this, this.context.applicationContext.getText(resId), duration)
    if (actionText != null && actionListener != null)
        snack.setAction(actionText, actionListener)
    return snack
}

fun View.showSnackbar(
    resId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionText: Int? = null,
    actionListener: View.OnClickListener? = null
) {
    this.createSnackbar(resId, duration, actionText, actionListener).show()
}

fun String.compareRawPhoneNumbers(number: String) = this.dirtyStringToNumbers() == number.dirtyStringToNumbers()

fun String.dirtyStringToNumbers() = replace(Regex("[^0-9]+"), "")

fun <T> Collection<T>.contentEquals(c: Collection<T>?) =
    c?.let { size == c.size && containsAll(c) } ?: false

fun View.begone() {
    visibility = GONE
}

fun View.showUp() {
    visibility = VISIBLE
}

fun <T : Any> List<T>.toArrayList() = this.toMutableList() as ArrayList

fun TextInputEditText.showKeyboard(activity: Activity) {
    requestFocus()
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        this,
        InputMethodManager.SHOW_IMPLICIT
    )
}

