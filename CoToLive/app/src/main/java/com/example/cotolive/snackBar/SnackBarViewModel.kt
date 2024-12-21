package com.example.cotolive.snackBar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SnackbarViewModel : ViewModel() {
    // 控制是否显示 Snackbar
    var showSnackbar by mutableStateOf(false)
    var isCorrect by mutableStateOf(false)
    var snackbarMessage by mutableStateOf("")

    // 显示 Snackbar 的方法
    fun showOKSnackbar(message: String) {
        isCorrect = true
        snackbarMessage = message
        showSnackbar = true
    }

    fun showErrSnackbar(message: String) {
        isCorrect = false
        snackbarMessage = message
        showSnackbar = true
    }

    // 隐藏 Snackbar
    fun hideSnackbar() {
        showSnackbar = false
    }
}