package com.example.cotolive.network

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL =
    "http://10.0.2.2:8088/"

// 数据模型：定义发送的消息内容
data class SignUpRequestMessage(
    @SerializedName("Name") val name: String,
    @SerializedName("Mail") val mail: String,
    @SerializedName("PassWord") val passWord: String
)

// 数据模型：定义服务器的响应
data class SignUpResponseMessage(
    val status: String, // 响应状态，例如 "success" 或 "error"
    val message: String // 响应详细信息，例如 "注册成功" 或错误原因
)

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface CoToLiveApiService {
    @POST("SignUp")
    suspend fun usrSignUp(@Body signUpReq: SignUpRequestMessage): SignUpResponseMessage
}

object CoToLiveApi {
    val retrofitService: CoToLiveApiService by lazy {
        retrofit.create(CoToLiveApiService::class.java)
    }
}