package com.example.juejin.core.common

import android.util.Log

/**
 * Android 平台日志实现
 * 输出到 Logcat
 */
actual object Logger {
    actual fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }
    
    actual fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }
    
    actual fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }
    
    actual fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }
    
    actual fun e(tag: String, msg: String, throwable: Throwable) {
        Log.e(tag, msg, throwable)
    }
}
