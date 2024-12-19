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
import com.example.cotolive.ui.theme.CoToLiveTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cotolive.navigation.AppNavigation


@Composable
fun SignUpScreenLayout(modifier: Modifier = Modifier, navController: NavController) {
    var userMail by remember { mutableStateOf("") }
    var userPwd by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var pwdReCheck by remember { mutableStateOf("") }
    var showPopUp by remember { mutableStateOf(false) }
    var checkResult by remember { mutableStateOf("") }
    var popUpOk by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val signUpViewModelInLayout: SignUpViewModel = viewModel()  // 这里使用 viewModel() 获取 ViewModel 实例
    val signUpState = signUpViewModelInLayout.signUpUiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp)
            .padding(top = 80.dp),
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
                .height(60.dp),
            value = userMail,
            onValueChange = { userMail = it },
            enabled = !isLoading,
            singleLine = true,
            label = {
                Text(
                    text = "账号",
                    fontSize = 22.sp,
                )
            },
            placeholder = {
                Text(
                    text = "邮箱",
                    fontSize = 14.sp,
                )
            },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(60.dp),
            value = userName,
            onValueChange = { userName = it },
            enabled = !isLoading,
            singleLine = true,
            label = {
                Text(
                    text = "名称",
                    fontSize = 22.sp,
                )
            },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            )
        )


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .height(60.dp)
                .background(color = Color.Transparent),
            value = userPwd,
            singleLine = true,
            onValueChange = { userPwd = it },
            enabled = !isLoading,
            label = {
                Text(
                    text = "密码",
                    fontSize = 22.sp,
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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(60.dp)
                .background(color = Color.Transparent),
            value = pwdReCheck,
            singleLine = true,
            onValueChange = { pwdReCheck = it },
            enabled = !isLoading,
            label = {
                Text(
                    text = "确认密码",
                    fontSize = 22.sp,
                )
            },
            placeholder = {
                Text(
                    text = "请再次输入密码",
                    fontSize = 14.sp
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
                .padding(top = 40.dp, start = 5.dp, end = 5.dp),
            onClick = {
                popUpOk = false
                isLoading = true
                checkResult = checkSignUpInputContent(userMail, userName, userPwd, pwdReCheck)
                if (checkResult != "") {
                    // 前端输入不合法，显示错误信息
                    showPopUp = true
                    isLoading = false
                } else {
                    // 调用后端进行注册
                    signUpViewModelInLayout.postUsrSignUp(userName, userMail, userPwd)
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(
                "注 册",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Cursive,
                fontSize = 20.sp
            )
        }

        AlertPopup(modifier = Modifier, showPopUp, checkResult, { showPopUp = false }, popUpOk)

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 30.dp).align(Alignment.CenterHorizontally))
        }

        // 当 signUpUiState 改变时重新执行
        LaunchedEffect(signUpState) {
            when (signUpState) {
                is SignUpUiState.Error -> {
                    // 后端错误，显示错误信息
                    Log.e("SignUp", "注册失败")
                    checkResult = signUpState.message
                    showPopUp = true
                }
                is SignUpUiState.Success -> {
                    // 注册成功，显示成功信息
                    Log.d("SignUp", "注册成功")
                    checkResult = "注册成功"
                    showPopUp = true
                    popUpOk = true
                }
                else -> { /* Loading 状态无需处理 */ }
            }
        }

        LaunchedEffect(signUpViewModelInLayout.signUpCallCnt) {
            isLoading = false
        }
    }
}





@Composable
fun AlertPopup(modifier: Modifier = Modifier, showPopup: Boolean, checkResult: String, onDismiss: () -> Unit, isOk: Boolean) {
    if (checkResult == "") return
    if (showPopup) {
        // 启动协程在3秒后关闭弹窗
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            onDismiss()
        }

        // 弹窗内容
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .border(1.dp, Color.Transparent, RoundedCornerShape(10.dp))
                .background(if (isOk)  Color(0xA0D5E7B5) else Color(0xF0FFCCE1)),
        ) {
            Text(
                text = checkResult,
                modifier= Modifier.padding(4.dp),
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

fun checkSignUpInputContent (userMail: String, userName: String, userPwd: String, pwdRecheck: String): String {
    if (userMail == "")
        return "邮箱不能为空！"
    if (userName == "")
        return "用户名不能为空！"
    if (userPwd == "")
        return "密码不能为空！"
    if (userPwd != pwdRecheck)
        return "您两次输入的密码不一致！"
    return ""
}




@Preview
@Composable
fun SignUpScreenPreview() {
    CoToLiveTheme {
        val navController = rememberNavController()
        AppNavigation(navController)
        SignUpScreenLayout(navController = navController)
    }
}