package com.example.cotolive.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotolive.network.ArticleAbstract
import com.example.cotolive.network.CoToLiveApi
import com.example.cotolive.network.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface FetchAbstractUiState {
    data class Success(val message: String) : FetchAbstractUiState
    data class Error(val message: String) : FetchAbstractUiState
    object Loading : FetchAbstractUiState
}

class FetchAbstractsViewModel : ViewModel() {
    var fetchAbstractUiState: FetchAbstractUiState by mutableStateOf(FetchAbstractUiState.Loading)
    var fetchAbstractsResults : List<ArticleAbstract> by mutableStateOf(emptyList())
        private set


    fun getArticleAbstracts() {
        viewModelScope.launch {
            fetchAbstractUiState = FetchAbstractUiState.Loading
            try {
                Log.d("FetchAbstractViewModel", "trying to send Abstract fetch")
                val fetchAbstractResponse = CoToLiveApi.retrofitService.articleAbstractsFetch()
                Log.d("FetchAbstractViewModel", "Abstract fetch is sent")
                fetchAbstractUiState = FetchAbstractUiState.Success(
                    "Success: 查找成功"
                )
                fetchAbstractsResults = fetchAbstractResponse.message
            } catch (e: IOException) {
                Log.e("FetchAbstractViewModel", "Network error", e)
                fetchAbstractUiState = FetchAbstractUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("FetchAbstractViewModel", "HTTP error", e)
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
                fetchAbstractUiState = FetchAbstractUiState.Error(errorMessage)
            }
        }
    }
}
