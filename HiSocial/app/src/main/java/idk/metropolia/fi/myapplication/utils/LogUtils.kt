package com.example.ahao9.socialevent.utils

import android.util.Log

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 17:10 2018/10/21
 * @ Description：Build for Metropolia project
 */
object LogUtils {

    var isDebug = true
    private val TAG = "hero"

    fun i(msg: String) {
        if (isDebug)
            Log.i(TAG, msg)
    }

    fun d(msg: String) {
        if (isDebug)
            Log.d(TAG, msg)
    }

    fun e(msg: String) {
        if (isDebug)
            Log.e(TAG, msg)
    }

    fun v(msg: String) {
        if (isDebug)
            Log.v(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        if (isDebug)
            Log.i(tag, msg)
    }

    fun d(tag: String, msg: String) {
        if (isDebug)
            Log.i(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (isDebug)
            Log.i(tag, msg)
    }

    fun v(tag: String, msg: String) {
        if (isDebug)
            Log.i(tag, msg)
    }
}
