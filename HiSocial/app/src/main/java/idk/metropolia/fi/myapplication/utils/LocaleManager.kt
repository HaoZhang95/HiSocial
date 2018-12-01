package idk.metropolia.fi.myapplication.utils

import android.content.Context
import android.os.Build
import com.example.ahao9.socialevent.utils.LogUtils
import java.util.*

class LocaleManager(context: Context) {

    private val context: Context = context
    private val prefManager: PrefManager = PrefManager(context)

    // change + save new locale
    fun changeLocale(language: String) {
        setCurrentLocale(language)
        prefManager.setLocale(language)
    }

    // get latest locale set
    fun getLocale() {
        setCurrentLocale(prefManager.getLocale())
    }

    // set current locale settings in system
    private fun setCurrentLocale(lang: String) {

        LogUtils.e("设置成: $lang")

        val locale = Locale(lang)
        val resources = context.resources
        val configuration = resources.configuration

        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }

        resources.updateConfiguration(configuration, resources.displayMetrics)

        // context.recreate()
    }
}