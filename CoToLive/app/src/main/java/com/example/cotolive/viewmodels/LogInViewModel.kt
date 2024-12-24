package com.example.cotolive.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotolive.network.CoToLiveApi
import com.example.cotolive.network.ErrorResponse
import com.example.cotolive.network.LogInPostMessage
import com.example.cotolive.network.TokenManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface LogInUiState {
    data class Success(val message: String) : LogInUiState
    data class Error(val message: String) : LogInUiState
    object Loading : LogInUiState
}

class LogInViewModel : ViewModel() {
    var logInUiState: LogInUiState by mutableStateOf(LogInUiState.Loading)
    var logInCallCnt: Int by mutableIntStateOf(0)
        private set


    fun postUsrLogIn(mail: String, password: String) {
        viewModelScope.launch {
            val logInReqMsg = LogInPostMessage(mail, password)
            logInUiState = LogInUiState.Loading
            try {
                val logInResponse = CoToLiveApi.retrofitService.usrLogIn(logInReqMsg)
                TokenManager.token = logInResponse.token
                logInUiState = LogInUiState.Success(
                    "登录成功, ${logInResponse.name}，你好。"
                )
            } catch (e: IOException) {
                Log.e("LogInViewModel", "Network error", e)
                logInUiState = LogInUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("LogInViewModel", "HTTP error", e)
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

                logInUiState = LogInUiState.Error(errorMessage)
            }
            logInCallCnt ++
        }
    }

    fun resetState () {
        logInUiState = LogInUiState.Loading
    }
}
