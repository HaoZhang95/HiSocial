package com.example.ahao9.socialevent.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import idk.metropolia.fi.myapplication.R

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 23:43 2018/10/18
 * @ Description：Build for Metropolia project
 */
object MyToast {

    var isShow = true
    var toast: Toast? = null

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     */
    fun show(context: Context, message: String?) {
        if (isShow) {
            toast = Toast(context)
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.mtoast_layout, null)
            val tv = view.findViewById(R.id.toast_tv) as TextView
            if (message != null) {
                tv.text = message
            }
            toast!!.view = view
            toast!!.duration = Toast.LENGTH_SHORT
            toast!!.show()
        }
    }

    fun cancel() {
        if (toast != null) {
            toast!!.cancel()
        }
    }
}
