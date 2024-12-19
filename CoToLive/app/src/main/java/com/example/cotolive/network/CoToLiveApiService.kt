package com.example.cotolive.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

private const val BASE_URL = "http://10.0.2.2:8088/"

// 拦截器，用于在请求头中添加 Authorization 信息
class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 获取全局 token
        val token = TokenManager.token

        // 如果 token 存在，则在请求头中附加 Authorization
        val newRequest = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "$token")  // 添加 Authorization 头
                .build()
        } else {
            chain.request()  // 如果没有 token，直接发出请求
        }

        return chain.proceed(newRequest)  // 继续执行请求
    }
}

// 创建 OkHttpClient 并添加拦截器
val client = OkHttpClient.Builder()
    .addInterceptor(AuthenticationInterceptor())  // 添加认证拦截器
    .build()

// 创建 Retrofit 实例
val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)  // 设置基础 URL
    .client(client)  // 使用带认证拦截器的 OkHttpClient
    .addConverterFactory(GsonConverterFactory.create())  // 设置解析器
    .build()

// 定义 Retrofit 接口
interface CoToLiveApiService {
    @POST("SignUp")
    suspend fun usrSignUp(@Body signUpReq: SignUpRequestMessage): SignUpPostMessage

    @POST("LogIn")
    suspend fun usrLogIn(@Body logInReq: LogInPostMessage): LogInResponseMessage

    @GET("/UserPage/GetArticleAbstract")
    suspend fun articleAbstractsFetch(): ArticleAbstractResponseMessage

    @GET("/UserPage/ReadArticle")
    suspend fun articleRead(@Query("ArticleID") articleID: Int): ArticleResponseMessage

    @PUT("/UserPage/ModifyArticle")
    suspend fun articleModify(@Body article: ArticleSent): ArticlePostResponseMessage

    @POST("/UserPage/PostArticle")
    suspend fun articlePost(@Body article: ArticleSent): ArticlePostResponseMessage
}

// 单例对象，用于提供 Retrofit 服务
object CoToLiveApi {
    val retrofitService: CoToLiveApiService by lazy {
        retrofit.create(CoToLiveApiService::class.java)
    }
}
