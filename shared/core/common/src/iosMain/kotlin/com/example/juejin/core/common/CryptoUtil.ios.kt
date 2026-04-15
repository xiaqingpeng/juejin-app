package com.example.juejin.core.common

/**
 * iOS 平台的 MD5 实现
 */
actual object CryptoUtil {
    actual fun md5(input: String): String {
        return MD5.hash(input)
    }
}
