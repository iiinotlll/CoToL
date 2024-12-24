package com.example.cotolive.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotolive.network.ArticleDel
import com.example.cotolive.network.ArticleGet
import com.example.cotolive.network.ArticleSent
import com.example.cotolive.network.CoToLiveApi
import com.example.cotolive.network.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface ArticleManageUiState {
    data class Success(val message: String) : ArticleManageUiState
    data class Error(val message: String) : ArticleManageUiState
    object Loading : ArticleManageUiState
}

class ArticleManageViewModel : ViewModel() {
    var articleReadUiState: ArticleManageUiState by mutableStateOf(ArticleManageUiState.Loading)
    var articleReadResult : ArticleGet by mutableStateOf(ArticleGet(aid = 0, belongsToUid = 0, title = "", content = ""))
        private set


    fun readArticle(aid: Int) {
        viewModelScope.launch {
            articleReadUiState = ArticleManageUiState.Loading
            try {
                Log.d("ArticleManageViewModel", "trying to send Abstract fetch")
                val articleReadResponse = CoToLiveApi.retrofitService.articleRead(aid)
                Log.d("ArticleManageViewModel", "Abstract fetch is sent")
                articleReadUiState = ArticleManageUiState.Success(
                    "Success: 查找成功"
                )
                articleReadResult = articleReadResponse.message.copy()
            } catch (e: IOException) {
                Log.e("ArticleManageViewModel", "Network error", e)
                articleReadUiState = ArticleManageUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("ArticleManageViewModel", "HTTP error", e)
                // 这里可以获取到 HTTP 错误的详细信息
                val errorMessage = e.response()?.errorBody()?.string() ?: "服务器错误，请稍后再试"
                articleReadUiState = ArticleManageUiState.Error(errorMessage)
            }
        }
    }

    var articleModifyUiState: ArticleManageUiState by mutableStateOf(ArticleManageUiState.Loading)
        private set

    fun modifyArticle(articleSent: ArticleSent) {
        viewModelScope.launch {
            articleModifyUiState = ArticleManageUiState.Loading
            try {
                Log.d("ArticleManageViewModel", "trying to send modify article")
                val articleModifyResponse = CoToLiveApi.retrofitService.articleModify(articleSent)
                Log.d("ArticleManageViewModel", "article modify is sent")
                articleModifyUiState = ArticleManageUiState.Success(
                    "Success: 查找成功"
                )
            } catch (e: IOException) {
                Log.e("ArticleManageViewModel", "Network error", e)
                articleModifyUiState = ArticleManageUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("ArticleManageViewModel", "HTTP error", e)
                // 这里可以获取到 HTTP 错误的详细信息
                val errorMessage = try {
                    val errorBody = e.response()?.errorBody()?.string()
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                    errorResponse?.message ?: "服务器错误，请稍后再试"
                } catch (ex: Exception) {
                    // 如果解析失败，使用通用错误信息
                    "解析错误，请稍后再试"
                }
                articleModifyUiState = ArticleManageUiState.Error(errorMessage)
            }
        }
    }

    var articleNewPostUiState: ArticleManageUiState by mutableStateOf(ArticleManageUiState.Loading)
        private set

    fun postNewArticle(articleSent: ArticleSent) {
        viewModelScope.launch {
            articleNewPostUiState = ArticleManageUiState.Loading
            try {
                Log.d("ArticleManageViewModel", "trying to post new article")
                val articleModifyResponse = CoToLiveApi.retrofitService.articlePost(articleSent)
                Log.d("ArticleManageViewModel", "post is sent")
                articleNewPostUiState = ArticleManageUiState.Success(
                    "Success: 发布成功"
                )
            } catch (e: IOException) {
                Log.e("ArticleManageViewModel", "Network error", e)
                articleNewPostUiState = ArticleManageUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("ArticleManageViewModel", "HTTP error", e)
                // 这里可以获取到 HTTP 错误的详细信息
                val errorMessage = try {
                    val errorBody = e.response()?.errorBody()?.string()
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                    errorResponse?.message ?: "服务器错误，请稍后再试"
                } catch (ex: Exception) {
                    // 如果解析失败，使用通用错误信息
                    "解析错误，请稍后再试"
                }
                articleNewPostUiState = ArticleManageUiState.Error(errorMessage)
            }
        }
    }

    var articleDelUiState: ArticleManageUiState by mutableStateOf(ArticleManageUiState.Loading)
        private set

    fun delArticle(aid: Int) {
        viewModelScope.launch {
            articleDelUiState = ArticleManageUiState.Loading
            try {
                Log.d("ArticleManageViewModel", "trying to del article")
                val articleModifyResponse = CoToLiveApi.retrofitService.articleDelete(ArticleDel(aid))
                Log.d("ArticleManageViewModel", "article del is sent")
                articleDelUiState = ArticleManageUiState.Success(
                    "Success: 删除成功"
                )
            } catch (e: IOException) {
                Log.e("ArticleManageViewModel", "Network error", e)
                articleDelUiState = ArticleManageUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("ArticleManageViewModel", "HTTP error", e)
                // 这里可以获取到 HTTP 错误的详细信息
                val errorMessage = try {
                    val errorBody = e.response()?.errorBody()?.string()
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                    errorResponse?.message ?: "服务器错误，请稍后再试"
                } catch (ex: Exception) {
                    // 如果解析失败，使用通用错误信息
                    "解析错误，请稍后再试"
                }
                articleDelUiState = ArticleManageUiState.Error(errorMessage)
            }
        }
    }
}
