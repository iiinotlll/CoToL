package com.example.cotolive.screen

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.res.painterResource
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
import com.example.cotolive.R
import com.example.cotolive.navigation.AppNavigation
import com.example.cotolive.navigation.CoToLScreen
import com.example.cotolive.snackBar.SnackbarViewModel


@Composable
fun SignUpScreenLayout(
    modifier: Modifier = Modifier,
    navController: NavController,
    snackbarViewModel: SnackbarViewModel
) {
    var userMail by remember { mutableStateOf("") }
    var userPwd by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var pwdReCheck by remember { mutableStateOf("") }

    var checkResult by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val signUpViewModelInLayout: SignUpViewModel = viewModel()  // 这里使用 viewModel() 获取 ViewModel 实例
    val signUpState = signUpViewModelInLayout.signUpUiState

    IconButton(
        modifier = modifier
            .padding(top = 30.dp)
            .height(30.dp),
        onClick = { navController.popBackStack() }) {
        Icon(
            painter = painterResource(R.drawable.back),
            contentDescription = "Back Button"
        )
    }

    Column(
        modifier = modifier
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
            textStyle = TextStyle(fontSize = 18.sp),
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
            textStyle = TextStyle(fontSize = 18.sp),
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
                isLoading = true
                checkResult = checkSignUpInputContent(userMail, userName, userPwd, pwdReCheck)
                if (checkResult != "") {
                    // 前端输入不合法，显示错误信息
                    snackbarViewModel.showErrSnackbar(checkResult)
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

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        // 当 signUpUiState 改变时重新执行
        LaunchedEffect(signUpState) {
            when (signUpState) {
                is SignUpUiState.Error -> {
                    // 后端错误，显示错误信息
                    Log.e("SignUp", "注册失败")
                    checkResult = signUpState.message
                    snackbarViewModel.showErrSnackbar(checkResult)
                }

                is SignUpUiState.Success -> {
                    // 注册成功，显示成功信息
                    Log.d("SignUp", "注册成功")
                    checkResult = "注册成功"
                    snackbarViewModel.showOKSnackbar(checkResult)

                    navController.navigate(CoToLScreen.LogIn.name)
                }

                else -> { /* Loading 状态无需处理 */
                }
            }
        }

        LaunchedEffect(signUpViewModelInLayout.signUpCallCnt) {
            isLoading = false
        }
    }
}

fun checkSignUpInputContent(
    userMail: String,
    userName: String,
    userPwd: String,
    pwdRecheck: String
): String {
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
        val snackbarViewModel: SnackbarViewModel = viewModel()
        SignUpScreenLayout(navController = navController, snackbarViewModel = snackbarViewModel)
    }
}