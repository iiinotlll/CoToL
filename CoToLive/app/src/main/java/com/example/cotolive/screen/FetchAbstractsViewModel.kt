package com.example.cotolive.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotolive.network.ArticleAbstract
import com.example.cotolive.network.CoToLiveApi
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
                val errorMessage = e.response()?.errorBody()?.string() ?: "服务器错误，请稍后再试"
                fetchAbstractUiState = FetchAbstractUiState.Error(errorMessage)
            }
        }
    }
}
