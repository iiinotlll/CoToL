package com.example.cotolive.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreenLayout(modifier: Modifier = Modifier) {
    var userMail by remember { mutableStateOf("") }
    var userPwd by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .padding(30.dp)
            .fillMaxSize()
            .padding(top = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "CoToLive",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp),
            textAlign = TextAlign.Center,
            fontSize = 45.sp,
            fontFamily = FontFamily.Monospace
        )


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            value = userMail,
            onValueChange = { userMail = it },
            singleLine = true,
            label = {
                Text(
                    text = "账号",
                    fontSize = 23.sp,
                    modifier = Modifier
                )
            },
            placeholder = { Text(
                text = "邮箱",
            ) },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            )
        )


        OutlinedTextField (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(80.dp),
            value = userPwd,
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
                focusedIndicatorColor = Color.LightGray, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray, // 非聚焦时的指示器颜色
                unfocusedContainerColor = Color.White,
            ),
            visualTransformation = PasswordVisualTransformation()
        )


        Button(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 5.dp, end = 5.dp),
            onClick = { logInClick(userMail, userPwd) },
        ) {
            Text(
                "登 录",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Cursive,
                fontSize = 20.sp
            )
        }
    }
}

private fun logInClick(email: String, pwd: String) {
    Log.d("email", email)
    Log.d("pwd",pwd)

    GlobalScope.launch(Dispatchers.IO) {
        sendLoginRequest(email, pwd)
    }
}


private suspend fun sendLoginRequest(email: String, password: String): Boolean {
    return try {
        // 使用 http:// 而非 https://
        val url = URL("http://10.0.2.2:8088/Login")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.doOutput = true

        // 构造 JSON 数据
        val jsonInput = """{
            "Mail": "$email",
            "PassWord": "$password"
        }""".trimIndent()

        // 写入数据
        connection.outputStream.use { os ->
            os.write(jsonInput.toByteArray())
            os.flush()
        }

        Log.d("LogSent", "LogIn message is sent.")

        // 读取响应
        val responseCode = connection.responseCode
        responseCode == 200 // 返回是否成功
    } catch (e: Exception) {
        Log.e("Error", "Exception occurred: ${e.message}")
        e.printStackTrace()
        false
    }
}


@Preview
@Composable
fun LogInScreenPreview() {
    CoToLiveTheme  {
        LogInScreenLayout()
    }
}