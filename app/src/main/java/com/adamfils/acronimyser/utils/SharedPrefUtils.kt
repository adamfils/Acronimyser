package com.adamfils.acronimyser.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefUtils {

    private const val CACHE_PREF = "cache_pref"
    const val QUERY_KEY = "query_key"
    const val RESULT_KEY = "result_key"

    private fun Context.getSecureSharedPreference(name: String = CACHE_PREF): SharedPreferences {
        return getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun Context.getPrefString(key: String): String {
        return getSecureSharedPreference().getString(key, "") ?: ""
    }

    fun Context.setPrefString(key: String, value: String = "") {
        if (value.isEmpty()) return
        val editor = getSecureSharedPreference().edit()
        editor.putString(key, value)
        editor.apply()
    }
}