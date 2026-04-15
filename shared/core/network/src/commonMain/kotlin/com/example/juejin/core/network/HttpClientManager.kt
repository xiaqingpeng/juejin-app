package com.example.juejin.core.network

import com.example.juejin.core.common.Logger
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
        Logger.d("HttpClient", "GET: $url")
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
        Logger.d("HttpClient", "POST: $url")
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
        Logger.d("HttpClient", "PUT: $url")
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
        Logger.d("HttpClient", "DELETE: $url")
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
        Logger.d("HttpClient", "PATCH: $url")
        return client.patch(url) {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
            builder()
        }
    }
    
    /**
     * 解析响应体为指定类型
     * @param response HTTP 响应
     * @return 解析后的对象
     */
    suspend fun <T> parseResponse(response: HttpResponse, deserializer: kotlinx.serialization.DeserializationStrategy<T>): T {
        val responseBody = response.bodyAsText()
        Logger.d("HttpClient", "Response: $responseBody")
        
        // 检查响应体是否为空
        if (responseBody.isBlank()) {
            throw IllegalArgumentException("Empty response body")
        }
        
        return json.decodeFromString(deserializer, responseBody)
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
