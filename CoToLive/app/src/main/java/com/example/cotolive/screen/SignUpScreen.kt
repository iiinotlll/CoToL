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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenLayout(modifier: Modifier = Modifier) {
    var userMail by remember { mutableStateOf("") }
    var userPwd by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var pwdReCheck by remember { mutableStateOf("") }
    var showPopUp by remember { mutableStateOf(false) }
    var checkResult by remember { mutableStateOf("") }

    val signUpViewModel: SignUpViewModel = viewModel()  // 这里使用 viewModel() 获取 ViewModel 实例
    val signUpUiState = signUpViewModel.signUpUiState

    // 获取 CoroutineScope
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxSize()
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
                checkResult = checkSignUpInputText(userMail, userName, userPwd, pwdReCheck)
                if (checkResult != "") {
                    // 前端输入不合法，显示错误信息
                    showPopUp = true
                } else {
                    // 调用后端进行注册
                    signUpViewModel.postUsrSignUp(userName, userMail, userPwd)
                    when (val state = signUpViewModel.signUpUiState) {
                        is SignUpUiState.Error -> {
                            // 后端错误，显示错误信息
                            checkResult = "注册失败，请稍后再试"
                            showPopUp = true
                        }
                        is SignUpUiState.Success -> {
                            // 注册成功，显示成功信息
                            checkResult = state.message
                            showPopUp = true
                        }
                        else -> { /* Loading 状态无需处理 */ }
                    }
                }
            },
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

        PasswordErrorPopup(modifier = Modifier, showPopUp, checkResult, { showPopUp = false })
    }
}





@Composable
fun PasswordErrorPopup(modifier: Modifier = Modifier, showPopup: Boolean, checkResult: String, onDismiss: () -> Unit) {
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
                .border(1.dp, Color.DarkGray, RoundedCornerShape(5.dp))
                .background(Color(0x30D3545F)),
        ) {
            Text(
                text = checkResult,
                modifier= Modifier.padding(4.dp),
                color = Color.Black,
                fontSize = 22.sp
            )
        }
    }
}

fun checkSignUpInputText (userMail: String, userName: String, userPwd: String, pwdRecheck: String): String {
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
        SignUpScreenLayout()
    }
}