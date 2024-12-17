package com.example.cotolive.network

import com.google.gson.annotations.SerializedName

// 数据模型：定义发送的消息内容
data class ArticleAbstractGetMessage(
    @SerializedName("UID") val uid: Int
)

// 数据模型：定义服务器的响应
data class ArticleAbstractResponseMessage(
    val status: String, // 响应状态，例如 "success" 或 "error"
    val message: String // 响应详细信息，例如 "注册成功" 或错误原因
)