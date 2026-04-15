package com.example.juejin.core.common

actual object CryptoUtil {
    actual fun md5(input: String): String {
        return MD5.hash(input)
    }
}
