package com.example.juejin.network

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * API 配置
 */
object ApiConfig {
    const val BASE_URL = "http://120.48.95.51:7001"
    
    /**
     * 构建完整 URL
     * @param path 接口路径（如 /api/analytics/events）
     * @return 完整 URL
     */
    fun buildUrl(path: String): String {
        return "$BASE_URL$path"
    }
    
}

/**
 * 网络请求客户端封装
 * 支持 GET、POST、PUT、DELETE、PATCH 方法
 */
object HttpClientManager {
    
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
    
    val client: HttpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
    
    /**
     * GET 请求
     * @param url 请求地址
     * @param builder 请求配置
     * @return HttpResponse 响应对象
     */
    suspend fun get(
        url: String,
        builder: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        Logger.d("HttpClient") { "GET: $url" }
        return client.get(url, builder)
    }
    
    /**
     * POST 请求
     * @param url 请求地址
     * @param body 请求体
     * @param builder 请求配置
     * @return HttpResponse 响应对象
     */
    suspend fun post(
        url: String,
        body: Any? = null,
        builder: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        Logger.d("HttpClient") { "POST: $url" }
        return client.post(url) {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
            builder()
        }
    }
    
    /**
     * PUT 请求
     * @param url 请求地址
     * @param body 请求体
     * @param builder 请求配置
     * @return HttpResponse 响应对象
     */
    suspend fun put(
        url: String,
        body: Any? = null,
        builder: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        Logger.d("HttpClient") { "PUT: $url" }
        return client.put(url) {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
            builder()
        }
    }
    
    /**
     * DELETE 请求
     * @param url 请求地址
     * @param builder 请求配置
     * @return HttpResponse 响应对象
     */
    suspend fun delete(
        url: String,
        builder: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        Logger.d("HttpClient") { "DELETE: $url" }
        return client.delete(url, builder)
    }
    
    /**
     * PATCH 请求
     * @param url 请求地址
     * @param body 请求体
     * @param builder 请求配置
     * @return HttpResponse 响应对象
     */
    suspend fun patch(
        url: String,
        body: Any? = null,
        builder: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        Logger.d("HttpClient") { "PATCH: $url" }
        return client.patch(url) {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
            builder()
        }
    }
    
    /**
     * 解析响应体为指定类型
     * @param response HTTP 响应
     * @param deserializer 反序列化器
     * @return 解析后的对象
     */
    suspend inline fun <reified T> parseResponse(response: HttpResponse): T {
        val responseBody = response.bodyAsText()
        Logger.d("HttpClient") { "Response: $responseBody" }
        return json.decodeFromString<T>(responseBody)
    }
    
    /**
     * 获取响应体文本
     * @param response HTTP 响应
     * @return 响应体文本
     */
    suspend fun getResponseBody(response: HttpResponse): String {
        return response.bodyAsText()
    }
}
