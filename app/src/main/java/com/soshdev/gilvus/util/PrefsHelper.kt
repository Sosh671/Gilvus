package com.soshdev.gilvus.util

import android.content.Context
import androidx.core.content.edit

class PrefsHelper(private val context: Context) {

    private val prefsName = "Gilvus_prefs"
    private val keyToken = "token"
    private val prefs =
        context.applicationContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    fun putToken(token: String?) {
        prefs.edit {
            putString(keyToken, token)
            commit()
        }
    }

    fun getToken() = prefs.getString(keyToken, null)
}