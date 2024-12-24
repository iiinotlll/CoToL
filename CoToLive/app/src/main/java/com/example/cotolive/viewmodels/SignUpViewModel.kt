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
import com.example.cotolive.network.SignUpRequestMessage
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface SignUpUiState {
    data class Success(val message: String) : SignUpUiState
    data class Error(val message: String) : SignUpUiState
    object Loading : SignUpUiState
}

class SignUpViewModel : ViewModel() {
    var signUpUiState: SignUpUiState by mutableStateOf(SignUpUiState.Loading)
    var signUpCallCnt: Int by mutableIntStateOf(0)
        private set


    fun postUsrSignUp(name: String, mail: String, password: String) {
        viewModelScope.launch {
            val signUpReqMsg = SignUpRequestMessage(name, mail, password)
            signUpUiState = SignUpUiState.Loading
            try {
                val signUpResponse = CoToLiveApi.retrofitService.usrSignUp(signUpReqMsg)
                signUpUiState = SignUpUiState.Success(
                    "Success: 注册成功, ${signUpResponse.message}"
                )
            } catch (e: IOException) {
                Log.e("SignUpViewModel", "Network error", e)
                signUpUiState = SignUpUiState.Error("网络错误，请稍后再试")
            } catch (e: HttpException) {
                Log.e("SignUpViewModel", "HTTP error", e)
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
                signUpUiState = SignUpUiState.Error(errorMessage)
            }
            signUpCallCnt ++
        }
    }
}
