package com.example.juejin.lite.data.remote

import com.example.juejin.core.common.Logger
import com.example.juejin.core.network.HttpClientManager
import io.ktor.client.call.*
import io.ktor.client.request.*
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
            // 使用秒级时间戳
            val apiTime = (com.example.juejin.core.common.DateTimeUtil.currentTimeMillis() / 1000).toString()
            
            val url = "$BASE_URL/producttype/listforapp.html"
            
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
            
            // 解析响应
            val result: YiwugoCategoryListResponse = HttpClientManager.json.decodeFromString(responseBody)
            
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
            // 使用秒级时间戳
            val apiTime = (com.example.juejin.core.common.DateTimeUtil.currentTimeMillis() / 1000).toString()
            
            val url = "$BASE_URL/producttype/listforapp.html"
            
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
            
            // 解析为子分类列表
            val categoryResponse: YiwugoCategoryListResponse = HttpClientManager.json.decodeFromString(responseBody)
            
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
    
    /**
     * 获取推荐商品列表（用于购物车"猜你喜欢"）
     */
    suspend fun getRecommendedProducts(
        userId: String = "0106ba9a-af5f-4121-8880-a9e741807126",
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<ProductDto>> {
        return try {
            val apiId = generateUUID()
            // 使用秒级时间戳
            val apiTime = (com.example.juejin.core.common.DateTimeUtil.currentTimeMillis() / 1000).toString()
            
            // 生成签名（MD5）- 使用真正的 MD5
            val signString = "$apiTime$apiId$API_KEY"
            val apiSign = com.example.juejin.core.common.CryptoUtil.md5(signString)
            
            val url = "https://api.yiwugo.com/app/getMyCenterRecommencProductPi.htm"
            
            val response = HttpClientManager.get(url) {
                url {
                    parameters.append("currPage", page.toString())
                    parameters.append("pageSize", pageSize.toString())
                    parameters.append("userId", userId)
                    parameters.append("spm", "YXBpLnlpd3Vnby5jb20vYXBwL2dldE15Q2VudGVyUmVjb21tZW5jUHJvZHVjdFBpLmh0bQ==")
                    parameters.append("api_sign", apiSign)
                    parameters.append("api_key", API_KEY)
                    parameters.append("appid", "1")
                    parameters.append("qudao", "hm")
                    parameters.append("imei", DEVICE_ID)
                    parameters.append("api_t", apiTime)
                    parameters.append("api_id", apiId)
                }
                headers {
                    append("Host", "api.yiwugo.com")
                    append("accept", "application/json, text/plain, */*")
                    append("appversion", APP_VERSION)
                    append("content-type", "application/x-www-form-urlencoded")
                    append("cookie", "uid=harmonyos$APP_VERSION@@$DEVICE_ID;")
                    append("user-agent", "YiwugouApp/${APP_VERSION}HarmonyOS: Mobile")
                }
            }
            
            val responseBody = response.bodyAsText()
            
            // 解析响应
            val result: YiwugoRecommendResponse = HttpClientManager.json.decodeFromString(responseBody)
            
            val products = result.pi?.map { item ->
                ProductDto(
                    id = item.offerid ?: "",
                    title = item.subject ?: "",
                    pic = item.imageurl,
                    price = item.price,
                    minPrice = item.beginAmount,
                    maxPrice = null,
                    shopName = item.companyname,
                    viewCount = 0,
                    saleCount = 0
                )
            } ?: emptyList()
            
            Result.success(products)
        } catch (e: Exception) {
            Logger.e(TAG, "获取推荐商品失败: ${e.message}", e)
            Logger.e(TAG, "异常堆栈: ${e.stackTraceToString()}")
            Result.failure(e)
        }
    }
    
    /**
     * 用户登录
     */
    suspend fun login(
        userName: String,
        password: String
    ): Result<LoginResponseDto> {
        // 临时方案：使用模拟数据，跳过 API 调用
        // TODO: 修复签名算法后恢复真实 API 调用
        
        val mockResponse = LoginResponseDto(
            userId = "huawei_17835319",
            loginId = userName,
            bbsId = 20393495,
            nick = "173****2875",
            mobile = userName,
            userName = "173****2875",
            avatar = "http://ywgimg.yiwugo.com/complain/huawei_17835319/20260413/1xPkrnpd7BoUnpLm.jpeg",
            loginResult = "SUCCESS",
            uuid = "686cccb8d998142432466175040ad76f",
            openId = "MDH5jKJ9icnPang6YGZmb4rxURiapkA1nGJDzgv6VILfR0AA",
            userPrime = "非会员",
            shopId = null,
            shopName = null,
            email = null,
            userType = "0",
            signStatus = false,
            lastCacheTime = com.example.juejin.core.common.DateTimeUtil.currentTimeMillis(),
            emailStatus = false,
            mobileStatus = true,
            userStatus = "1",
            productCount = 0,
            postCount = 0,
            followCount = 0,
            replyCount = 0
        )
        
        return Result.success(mockResponse)
    }
    
    /**
     * 生成简单的 MD5 签名（跨平台实现）
     */
    private fun generateSimpleMD5(input: String): String {
        // 简单的哈希实现（不是真正的 MD5，只是为了测试）
        var hash = 0
        for (char in input) {
            hash = ((hash shl 5) - hash) + char.code
            hash = hash and hash // Convert to 32bit integer
        }
        return hash.toString(16).uppercase().padStart(32, '0')
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


/**
 * 义乌购推荐商品响应
 */
@Serializable
data class YiwugoRecommendResponse(
    @SerialName("pi") val pi: List<RecommendProductDto>? = null
)

/**
 * 推荐商品 DTO
 */
@Serializable
data class RecommendProductDto(
    @SerialName("offerid") val offerid: String? = null,
    @SerialName("subject") val subject: String? = null,
    @SerialName("imageurl") val imageurl: String? = null,
    @SerialName("price") val price: String? = null,
    @SerialName("beginAmount") val beginAmount: String? = null,
    @SerialName("companyname") val companyname: String? = null,
    @SerialName("companyid") val companyid: String? = null
)

/**
 * 登录响应 DTO
 */
@Serializable
data class LoginResponseDto(
    @SerialName("userId") val userId: String? = null,
    @SerialName("loginId") val loginId: String? = null,
    @SerialName("bbsId") val bbsId: Int? = null,
    @SerialName("bbsAuthor") val bbsAuthor: String? = null,
    @SerialName("bbsGroupId") val bbsGroupId: Int? = null,
    @SerialName("bbsCredits") val bbsCredits: String? = null,
    @SerialName("nick") val nick: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("mobile") val mobile: String? = null,
    @SerialName("userType") val userType: String? = null,
    @SerialName("signStatus") val signStatus: Boolean? = null,
    @SerialName("uuid") val uuid: String? = null,
    @SerialName("lastCacheTime") val lastCacheTime: Long? = null,
    @SerialName("loginUrl") val loginUrl: String? = null,
    @SerialName("emailStatus") val emailStatus: Boolean? = null,
    @SerialName("mobileStatus") val mobileStatus: Boolean? = null,
    @SerialName("userStatus") val userStatus: String? = null,
    @SerialName("loginResult") val loginResult: String? = null,
    @SerialName("productCount") val productCount: Int? = null,
    @SerialName("postCount") val postCount: Int? = null,
    @SerialName("followCount") val followCount: Int? = null,
    @SerialName("replyCount") val replyCount: Int? = null,
    @SerialName("contacter") val contacter: String? = null,
    @SerialName("telephone") val telephone: String? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("userName") val userName: String? = null,
    @SerialName("bbsName") val bbsName: String? = null,
    @SerialName("loginCount") val loginCount: String? = null,
    @SerialName("creditIntegrea") val creditIntegrea: String? = null,
    @SerialName("pemission") val pemission: Boolean? = null,
    @SerialName("marketCode") val marketCode: String? = null,
    @SerialName("openId") val openId: String? = null,
    @SerialName("avatar") val avatar: String? = null,
    @SerialName("lesseeStatus") val lesseeStatus: String? = null,
    @SerialName("shopId") val shopId: String? = null,
    @SerialName("shopName") val shopName: String? = null,
    @SerialName("imServer") val imServer: String? = null,
    @SerialName("imStatus") val imStatus: String? = null,
    @SerialName("userPrime") val userPrime: String? = null,
    @SerialName("operator") val operator: String? = null,
    @SerialName("registerTime") val registerTime: Long? = null,
    @SerialName("submarket") val submarket: String? = null,
    @SerialName("key") val key: String? = null,
    @SerialName("kdtFlag") val kdtFlag: Boolean? = null,
    @SerialName("oldUserid") val oldUserid: String? = null,
    @SerialName("userIdSub") val userIdSub: String? = null,
    @SerialName("ip") val ip: String? = null,
    @SerialName("companyUrl") val companyUrl: String? = null,
    @SerialName("dyy") val dyy: String? = null,
    @SerialName("sellFlag") val sellFlag: Boolean? = null,
    @SerialName("buyFlag") val buyFlag: Boolean? = null,
    @SerialName("dyyFlag") val dyyFlag: Boolean? = null,
    @SerialName("userRecommend") val userRecommend: Boolean? = null,
    @SerialName("thirdserviceFlag") val thirdserviceFlag: Boolean? = null,
    @SerialName("companyFlag") val companyFlag: Boolean? = null,
    @SerialName("subSellFlag") val subSellFlag: Boolean? = null
)
