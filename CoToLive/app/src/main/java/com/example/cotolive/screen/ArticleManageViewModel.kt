package com.example.cotolive.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotolive.network.ArticleGet
import com.example.cotolive.network.ArticleSent
import com.example.cotolive.network.CoToLiveApi
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
    var articleReadResult : ArticleGet by mutableStateOf(ArticleGet(aid = 0L, belongsToUid = 0L, title = "", content = ""))
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
                Log.d("ArticleManageViewModel", "trying to send Abstract fetch")
                val articleModifyResponse = CoToLiveApi.retrofitService.articleModify(articleSent)
                Log.d("ArticleManageViewModel", "Abstract fetch is sent")
                articleModifyUiState = ArticleManageUiState.Success(
                    "Success: 查找成功"
                )
            } catch (e: IOException) {
                Log.e("ArticleManageViewModel", "Network error", e)
                articleModifyUiState = ArticleManageUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("ArticleManageViewModel", "HTTP error", e)
                // 这里可以获取到 HTTP 错误的详细信息
                val errorMessage = e.response()?.errorBody()?.string() ?: "服务器错误，请稍后再试"
                articleModifyUiState = ArticleManageUiState.Error(errorMessage)
            }
        }
    }

    fun postNewArticle(articleSent: ArticleSent) {
        viewModelScope.launch {
            articleModifyUiState = ArticleManageUiState.Loading
            try {
                Log.d("ArticleManageViewModel", "trying to send Abstract fetch")
                val articleModifyResponse = CoToLiveApi.retrofitService.articlePost(articleSent)
                Log.d("ArticleManageViewModel", "Abstract fetch is sent")
                articleModifyUiState = ArticleManageUiState.Success(
                    "Success: 查找成功"
                )
            } catch (e: IOException) {
                Log.e("ArticleManageViewModel", "Network error", e)
                articleModifyUiState = ArticleManageUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("ArticleManageViewModel", "HTTP error", e)
                // 这里可以获取到 HTTP 错误的详细信息
                val errorMessage = e.response()?.errorBody()?.string() ?: "服务器错误，请稍后再试"
                articleModifyUiState = ArticleManageUiState.Error(errorMessage)
            }
        }
    }
}
