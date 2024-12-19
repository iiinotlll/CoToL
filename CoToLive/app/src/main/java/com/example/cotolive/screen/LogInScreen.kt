package com.example.cotolive.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cotolive.navigation.AppNavigation
import com.example.cotolive.ui.theme.CoToLiveTheme
import kotlin.math.log

@Composable
fun LogInScreenLayout(modifier: Modifier = Modifier, navController: NavController) {
    var userMail by remember { mutableStateOf("") }
    var userPwd by remember { mutableStateOf("") }

    var showPopUp by remember { mutableStateOf(false) }
    var checkResult by remember { mutableStateOf("") }
    var popUpOk by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val logInViewModelInLayout : LogInViewModel = viewModel()
    val logInUiState = logInViewModelInLayout.logInUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .padding(top = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Good Title",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 45.sp,
            fontFamily = FontFamily.Monospace
        )


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .height(70.dp),
            value = userMail,
            enabled = !isLoading,
            onValueChange = { userMail = it },
            singleLine = true,
            label = {
                Text(
                    text = "账号",
                    fontSize = 23.sp,
                )
            },
            placeholder = {
                Text(
                    text = "邮箱",
                    fontSize = 18.sp,
                )
            },
            textStyle = TextStyle(fontSize = 21.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            )
        )


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(60.dp)
                .background(color = Color.Transparent),
            value = userPwd,
            enabled = !isLoading,
            singleLine = true,
            onValueChange = { userPwd = it },
            label = {
                Text(
                    text = "密码",
                    fontSize = 23.sp,
                )
            },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            ),
            visualTransformation = PasswordVisualTransformation()
        )


        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 5.dp, end = 5.dp),
            enabled = !isLoading,
            onClick = {
                popUpOk = false
                isLoading = true
                checkResult = checkLogInInputContent(userMail, userPwd)
                if (checkResult != "") {
                    // 前端输入不合法，显示错误信息
                    showPopUp = true
                    isLoading = false
                } else {
                    logInViewModelInLayout.postUsrLogIn(mail = userMail, password = userPwd)
                }

            },
        ) {
            Text(
                "登 录",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Cursive,
                fontSize = 20.sp
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 5.dp, end = 5.dp)
                .border(
                width = 2.dp,              // 边框宽度
                color = Color.Gray,         // 边框颜色
                shape = RoundedCornerShape(30.dp) // 边框形状
                ),
            onClick = {
                    navController.navigate("SignUpScreen")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(
                "注 册",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Cursive,
                fontSize = 20.sp
            )
        }

        AlertPopup(showPopup = showPopUp, checkResult = checkResult, onDismiss = { showPopUp = false}, isOk = popUpOk)

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 30.dp).align(Alignment.CenterHorizontally))
        }

        LaunchedEffect(logInUiState) {
            when (logInUiState) {
                is LogInUiState.Error -> {
                    // 后端错误，显示错误信息
                    Log.e("LogIn", "登录失败")
                    checkResult = logInUiState.message
                    showPopUp = true
                }
                is LogInUiState.Success -> {
                    // 登录成功，显示成功信息
                    Log.d("LogIn", "登录成功")
                    checkResult = logInUiState.message
                    showPopUp = true
                    popUpOk = true
                }
                else -> { /* Loading 状态无需处理 */ }
            }
        }

        LaunchedEffect(logInViewModelInLayout.logInCallCnt) {
            isLoading = false
        }

    }
}


private fun checkLogInInputContent(mail: String, pwd: String): String{
    if (mail == "")
        return "邮箱不能为空！"
    if (pwd == "")
        return "密码不能为空！"
    return ""
}


@Preview
@Composable
fun LogInScreenPreview() {
    CoToLiveTheme {
        val navController = rememberNavController()
        AppNavigation(navController)
        LogInScreenLayout(navController = navController)
    }
}