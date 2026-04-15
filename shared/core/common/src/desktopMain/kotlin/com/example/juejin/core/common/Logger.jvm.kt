package com.example.juejin.core.common

/**
 * JVM/Desktop 平台日志实现
 * 输出到控制台
 */
actual object Logger {
    actual fun d(tag: String, msg: String) {
        println("D/$tag: $msg")
    }
    
    actual fun i(tag: String, msg: String) {
        println("I/$tag: $msg")
    }
    
    actual fun w(tag: String, msg: String) {
        println("W/$tag: $msg")
    }
    
    actual fun e(tag: String, msg: String) {
        println("E/$tag: $msg")
    }
    
    actual fun e(tag: String, msg: String, throwable: Throwable) {
        println("E/$tag: $msg")
        println("E/$tag: ${throwable.message}")
        throwable.printStackTrace()
    }
}
