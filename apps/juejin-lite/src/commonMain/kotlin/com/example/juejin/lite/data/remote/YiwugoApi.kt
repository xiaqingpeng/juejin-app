package com.example.juejin.lite.data.remote

import com.example.juejin.core.common.Logger
import com.example.juejin.core.network.HttpClientManager
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random

/**
 * 义乌购 API 服务
 * 使用共享的 HttpClientManager
 */
class YiwugoApi {
    
    companion object {
        private const val TAG = "YiwugoApi"
        private const val BASE_URL = "https://enapp.yiwugo.com"
        private const val APP_VERSION = "7.6.7"
        private const val API_KEY = "6fJMEmaz6xg4"
        private const val DEVICE_ID = "d27098a787b617f38f38cc93a41ce656"
        
        /**
         * 生成简单的 UUID（跨平台）
         */
        private fun generateUUID(): String {
            val chars = "0123456789abcdef"
            return buildString {
                repeat(8) { append(chars[Random.nextInt(16)]) }
                append('-')
                repeat(4) { append(chars[Random.nextInt(16)]) }
                append('-')
                append('4')
                repeat(3) { append(chars[Random.nextInt(16)]) }
                append('-')
                append(chars[8 + Random.nextInt(4)])
                repeat(3) { append(chars[Random.nextInt(16)]) }
                append('-')
                repeat(12) { append(chars[Random.nextInt(16)]) }
            }
        }
    }
    
    /**
     * 获取分类列表
     */
    suspend fun getCategories(upperType: String = "0"): Result<List<CategoryDto>> {
        return try {
            val apiId = generateUUID()
            val apiTime = com.example.juejin.core.common.DateTimeUtil.currentTimeMillis().toString()
            
            val url = "$BASE_URL/producttype/listforapp.html"
            Logger.d(TAG, "========== 获取分类列表 ==========")
            Logger.d(TAG, "URL: $url")
            Logger.d(TAG, "upperType: $upperType")
            Logger.d(TAG, "api_id: $apiId")
            Logger.d(TAG, "api_t: $apiTime")
            
            val response = HttpClientManager.get(url) {
                url {
                    parameters.append("webtype", "cn")
                    parameters.append("uppertype", upperType)
                    parameters.append("api_key", API_KEY)
                    parameters.append("appid", "1")
                    parameters.append("qudao", "hm")
                    parameters.append("imei", DEVICE_ID)
                    parameters.append("api_t", apiTime)
                    parameters.append("api_id", apiId)
                }
                headers {
                    append("Host", "enapp.yiwugo.com")
                    append("accept", "application/json, text/plain, */*")
                    append("appversion", APP_VERSION)
                    append("content-type", "application/x-www-form-urlencoded")
                    append("cookie", "uid=harmonyos$APP_VERSION@@$DEVICE_ID;")
                    append("user-agent", "YiwugouApp/${APP_VERSION}HarmonyOS: Mobile")
                }
            }
            
            val responseBody = response.bodyAsText()
            Logger.d(TAG, "Response Status: ${response.status}")
            Logger.d(TAG, "Response Headers: ${response.headers.entries().joinToString { "${it.key}: ${it.value}" }}")
            Logger.d(TAG, "Response Body Length: ${responseBody.length}")
            Logger.d(TAG, "Response Body (前500字符): ${responseBody.take(500)}")
            
            // 解析响应
            val result: YiwugoCategoryListResponse = HttpClientManager.json.decodeFromString(responseBody)
            Logger.d(TAG, "成功解析，分类数量: ${result.list.size}")
            if (result.list.isNotEmpty()) {
                Logger.d(TAG, "第一个分类: id=${result.list[0].id}, name=${result.list[0].type}, img=${result.list[0].img}")
            }
            Logger.d(TAG, "========================================")
            
            Result.success(result.list)
        } catch (e: Exception) {
            Logger.e(TAG, "获取分类列表失败: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 获取分类下的商品列表（实际是获取子分类）
     * 注意：义乌购 API 的 /producttype/listforapp.html 返回的是子分类，不是商品
     */
    suspend fun getProductsByCategory(
        categoryId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<YiwugoResponse<ProductListDto>> {
        return try {
            val apiId = generateUUID()
            val apiTime = com.example.juejin.core.common.DateTimeUtil.currentTimeMillis().toString()
            
            // 实际上这个接口返回的是子分类，不是商品
            // 我们先返回空列表，因为真实的商品接口需要更多参数
            val url = "$BASE_URL/producttype/listforapp.html"
            Logger.d(TAG, "========== 获取子分类（作为商品） ==========")
            Logger.d(TAG, "URL: $url")
            Logger.d(TAG, "uppertype (categoryId): $categoryId")
            Logger.d(TAG, "page: $page, pageSize: $pageSize")
            Logger.d(TAG, "api_id: $apiId")
            Logger.d(TAG, "api_t: $apiTime")
            
            val response = HttpClientManager.get(url) {
                url {
                    parameters.append("webtype", "cn")
                    parameters.append("uppertype", categoryId)  // 使用 uppertype 而不是 typeid
                    parameters.append("api_key", API_KEY)
                    parameters.append("appid", "1")
                    parameters.append("qudao", "hm")
                    parameters.append("imei", DEVICE_ID)
                    parameters.append("api_t", apiTime)
                    parameters.append("api_id", apiId)
                }
                headers {
                    append("Host", "enapp.yiwugo.com")
                    append("accept", "application/json, text/plain, */*")
                    append("appversion", APP_VERSION)
                    append("content-type", "application/x-www-form-urlencoded")
                    append("cookie", "uid=harmonyos$APP_VERSION@@$DEVICE_ID;")
                    append("user-agent", "YiwugouApp/${APP_VERSION}HarmonyOS: Mobile")
                }
            }
            
            val responseBody = response.bodyAsText()
            Logger.d(TAG, "Response Status: ${response.status}")
            Logger.d(TAG, "Response Headers: ${response.headers.entries().joinToString { "${it.key}: ${it.value}" }}")
            Logger.d(TAG, "Response Body Length: ${responseBody.length}")
            Logger.d(TAG, "Response Body (前500字符): ${responseBody.take(500)}")
            
            // 解析为子分类列表
            val categoryResponse: YiwugoCategoryListResponse = HttpClientManager.json.decodeFromString(responseBody)
            Logger.d(TAG, "成功解析，子分类数量: ${categoryResponse.list.size}")
            
            // 将子分类转换为"商品"（临时方案）
            val products = categoryResponse.list.map { category ->
                ProductDto(
                    id = category.id.toString(),
                    title = category.type,
                    pic = category.img,
                    price = null,
                    minPrice = null,
                    maxPrice = null,
                    shopName = category.uppertypeName,
                    viewCount = 0,
                    saleCount = 0
                )
            }
            
            Logger.d(TAG, "转换为商品数量: ${products.size}")
            if (products.isNotEmpty()) {
                Logger.d(TAG, "第一个商品: id=${products[0].id}, title=${products[0].title}, pic=${products[0].pic}")
            }
            Logger.d(TAG, "========================================")
            
            Result.success(YiwugoResponse(
                code = 200,
                status = null,
                msg = "success",
                message = null,
                data = ProductListDto(
                    list = products,
                    total = products.size,
                    page = page,
                    pageSize = pageSize
                ),
                result = null
            ))
        } catch (e: Exception) {
            Logger.e(TAG, "获取商品列表失败: ${e.message}", e)
            Result.failure(e)
        }
    }
}

/**
 * 义乌购分类列表响应
 */
@Serializable
data class YiwugoCategoryListResponse(
    @SerialName("list") val list: List<CategoryDto>
)

/**
 * 义乌购 API 响应基础结构
 * 支持多种可能的字段名
 */
@Serializable
data class YiwugoResponse<T>(
    @SerialName("code") val code: Int? = null,
    @SerialName("status") val status: Int? = null,
    @SerialName("msg") val msg: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: T? = null,
    @SerialName("result") val result: T? = null
) {
    // 获取实际的状态码
    fun getStatusCode(): Int = code ?: status ?: 0
    
    // 获取实际的消息
    fun getMessageText(): String = msg ?: message ?: ""
    
    // 获取实际的数据
    fun getResponseData(): T? = data ?: result
}

/**
 * 分类 DTO
 */
@Serializable
data class CategoryDto(
    @SerialName("id") val id: Int,
    @SerialName("type") val type: String,
    @SerialName("typeUrl") val typeUrl: String? = null,
    @SerialName("typeId") val typeId: String? = null,
    @SerialName("img") val img: String? = null,
    @SerialName("rank") val rank: String? = null,
    @SerialName("uppertype") val uppertype: String? = null,
    @SerialName("uppertypeName") val uppertypeName: String? = null,
    @SerialName("entype") val entype: String? = null,
    @SerialName("webtype") val webtype: String? = null
)

/**
 * 商品列表 DTO
 */
@Serializable
data class ProductListDto(
    @SerialName("list") val list: List<ProductDto>,
    @SerialName("total") val total: Int = 0,
    @SerialName("page") val page: Int = 1,
    @SerialName("pagesize") val pageSize: Int = 20
)

/**
 * 商品 DTO
 */
@Serializable
data class ProductDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("pic") val pic: String? = null,
    @SerialName("price") val price: String? = null,
    @SerialName("minprice") val minPrice: String? = null,
    @SerialName("maxprice") val maxPrice: String? = null,
    @SerialName("shopname") val shopName: String? = null,
    @SerialName("viewcount") val viewCount: Int = 0,
    @SerialName("salecount") val saleCount: Int = 0
)
