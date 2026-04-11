package com.example.juejin.network

import com.example.juejin.core.network.ApiConfig
import com.example.juejin.core.network.HttpClientManager

// 为了保持向后兼容，重新导出核心模块的类
@Deprecated("Use com.example.juejin.core.network.ApiConfig instead", ReplaceWith("com.example.juejin.core.network.ApiConfig"))
typealias ApiConfig = com.example.juejin.core.network.ApiConfig

@Deprecated("Use com.example.juejin.core.network.HttpClientManager instead", ReplaceWith("com.example.juejin.core.network.HttpClientManager"))
typealias HttpClientManager = com.example.juejin.core.network.HttpClientManager

