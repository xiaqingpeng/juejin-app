package com.example.juejin.util

/**
 * 跨平台日志工具
 * Android: 输出到 Logcat
 * iOS: 输出到 Xcode 控制台
 * JVM: 输出到控制台
 */
expect object Logger {
    /**
     * Debug 级别日志
     */
    fun d(tag: String, msg: String)
    
    /**
     * Info 级别日志
     */
    fun i(tag: String, msg: String)
    
    /**
     * Warning 级别日志
     */
    fun w(tag: String, msg: String)
    
    /**
     * Error 级别日志
     */
    fun e(tag: String, msg: String)
    
    /**
     * Error 级别日志（带异常）
     */
    fun e(tag: String, msg: String, throwable: Throwable)
}
