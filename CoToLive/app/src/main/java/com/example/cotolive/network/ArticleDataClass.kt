package com.example.cotolive.network

import com.google.gson.annotations.SerializedName

data class ArticleSent(
    @SerializedName("ArticleID") val aid: Int,
    @SerializedName("Title") val title: String,
    @SerializedName("Content") val content: String
)

data class ArticleGet(
    @SerializedName("AID") val aid: Long,
    @SerializedName("BelongsToUID") val belongsToUid: Long,
    @SerializedName("Title") val title: String,
    @SerializedName("Data") val content: String
)


// 数据模型：定义服务器的响应
data class ArticleResponseMessage(
    val status: String, // 响应状态，例如 "success" 或 "error"
    val message: ArticleGet // 响应详细信息
)

// 数据模型：定义服务器的响应
data class ArticlePostResponseMessage(
    val status: String, // 响应状态，例如 "success" 或 "error"
    val message: String // 响应详细信息
)