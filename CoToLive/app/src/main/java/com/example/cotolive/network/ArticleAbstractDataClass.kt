package com.example.cotolive.network

import com.google.gson.annotations.SerializedName


data class ArticleAbstract(
    @SerializedName("AID") val aid: Long,
    @SerializedName("BelongsToUID") val belongsToUid: Long,
    @SerializedName("Title") val title: String,
    @SerializedName("Data") val abstract: String
)


// 数据模型：定义服务器的响应
data class ArticleAbstractResponseMessage(
    val status: String, // 响应状态，例如 "success" 或 "error"
    val message: List<ArticleAbstract> // 响应详细信息
)