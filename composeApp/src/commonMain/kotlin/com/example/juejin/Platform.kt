package com.example.juejin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform