package com.example.juejin.core.common

import kotlin.experimental.and

/**
 * 跨平台加密工具
 * 使用纯 Kotlin 实现的 MD5
 */
expect object CryptoUtil {
    /**
     * MD5 加密
     */
    fun md5(input: String): String
}

/**
 * 纯 Kotlin 实现的 MD5（跨平台）
 */
object MD5 {
    fun hash(input: String): String {
        val bytes = input.encodeToByteArray()
        val md5Bytes = md5(bytes)
        return md5Bytes.joinToString("") { byte ->
            val value = byte.toInt() and 0xFF
            val hex = value.toString(16)
            if (hex.length == 1) "0$hex" else hex
        }
    }
    
    private fun md5(input: ByteArray): ByteArray {
        // MD5 constants
        val s = intArrayOf(
            7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
            5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
            4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
            6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
        )
        
        val K = intArrayOf(
            0xd76aa478.toInt(), 0xe8c7b756.toInt(), 0x242070db, 0xc1bdceee.toInt(),
            0xf57c0faf.toInt(), 0x4787c62a, 0xa8304613.toInt(), 0xfd469501.toInt(),
            0x698098d8, 0x8b44f7af.toInt(), 0xffff5bb1.toInt(), 0x895cd7be.toInt(),
            0x6b901122, 0xfd987193.toInt(), 0xa679438e.toInt(), 0x49b40821,
            0xf61e2562.toInt(), 0xc040b340.toInt(), 0x265e5a51, 0xe9b6c7aa.toInt(),
            0xd62f105d.toInt(), 0x02441453, 0xd8a1e681.toInt(), 0xe7d3fbc8.toInt(),
            0x21e1cde6, 0xc33707d6.toInt(), 0xf4d50d87.toInt(), 0x455a14ed,
            0xa9e3e905.toInt(), 0xfcefa3f8.toInt(), 0x676f02d9, 0x8d2a4c8a.toInt(),
            0xfffa3942.toInt(), 0x8771f681.toInt(), 0x6d9d6122, 0xfde5380c.toInt(),
            0xa4beea44.toInt(), 0x4bdecfa9, 0xf6bb4b60.toInt(), 0xbebfbc70.toInt(),
            0x289b7ec6, 0xeaa127fa.toInt(), 0xd4ef3085.toInt(), 0x04881d05,
            0xd9d4d039.toInt(), 0xe6db99e5.toInt(), 0x1fa27cf8, 0xc4ac5665.toInt(),
            0xf4292244.toInt(), 0x432aff97, 0xab9423a7.toInt(), 0xfc93a039.toInt(),
            0x655b59c3, 0x8f0ccc92.toInt(), 0xffeff47d.toInt(), 0x85845dd1.toInt(),
            0x6fa87e4f, 0xfe2ce6e0.toInt(), 0xa3014314.toInt(), 0x4e0811a1,
            0xf7537e82.toInt(), 0xbd3af235.toInt(), 0x2ad7d2bb, 0xeb86d391.toInt()
        )
        
        // Pre-processing
        val msgLen = input.size
        val numBlocks = ((msgLen + 8) ushr 6) + 1
        val totalLen = numBlocks shl 6
        val paddingBytes = ByteArray(totalLen - msgLen)
        paddingBytes[0] = 0x80.toByte()
        
        val bits = (msgLen.toLong() shl 3)
        for (i in 0..7) {
            paddingBytes[paddingBytes.size - 8 + i] = (bits ushr (i * 8)).toByte()
        }
        
        // Initialize hash values
        var a0 = 0x67452301
        var b0 = 0xEFCDAB89.toInt()
        var c0 = 0x98BADCFE.toInt()
        var d0 = 0x10325476
        
        // Process message in 512-bit chunks
        val message = input + paddingBytes
        for (chunkStart in message.indices step 64) {
            val M = IntArray(16)
            for (i in 0..15) {
                val offset = chunkStart + i * 4
                M[i] = (message[offset].toInt() and 0xFF) or
                       ((message[offset + 1].toInt() and 0xFF) shl 8) or
                       ((message[offset + 2].toInt() and 0xFF) shl 16) or
                       ((message[offset + 3].toInt() and 0xFF) shl 24)
            }
            
            var A = a0
            var B = b0
            var C = c0
            var D = d0
            
            for (i in 0..63) {
                val F: Int
                val g: Int
                when {
                    i < 16 -> {
                        F = (B and C) or (B.inv() and D)
                        g = i
                    }
                    i < 32 -> {
                        F = (D and B) or (D.inv() and C)
                        g = (5 * i + 1) % 16
                    }
                    i < 48 -> {
                        F = B xor C xor D
                        g = (3 * i + 5) % 16
                    }
                    else -> {
                        F = C xor (B or D.inv())
                        g = (7 * i) % 16
                    }
                }
                
                val temp = D
                D = C
                C = B
                B = B + leftRotate(A + F + K[i] + M[g], s[i])
                A = temp
            }
            
            a0 += A
            b0 += B
            c0 += C
            d0 += D
        }
        
        // Produce the final hash value
        val result = ByteArray(16)
        for (i in 0..3) {
            result[i] = (a0 ushr (i * 8)).toByte()
            result[i + 4] = (b0 ushr (i * 8)).toByte()
            result[i + 8] = (c0 ushr (i * 8)).toByte()
            result[i + 12] = (d0 ushr (i * 8)).toByte()
        }
        
        return result
    }
    
    private fun leftRotate(value: Int, shift: Int): Int {
        return (value shl shift) or (value ushr (32 - shift))
    }
}
