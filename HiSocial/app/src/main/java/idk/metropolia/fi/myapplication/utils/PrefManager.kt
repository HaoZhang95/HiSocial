package idk.metropolia.fi.myapplication.utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private val LOCALE_KEY = "locale"

    init {
        pref = context.getSharedPreferences("soundscape_v2", Context.MODE_PRIVATE)
        editor = pref!!.edit()
    }

    fun setLocale(locale: String) {
        editor!!.putString(LOCALE_KEY, locale).commit()
    }

    fun getLocale(): String {
        return pref!!.getString(LOCALE_KEY, "us")
    }
}