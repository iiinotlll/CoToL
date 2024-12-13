package com.example.cotolive.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreenLayout(modifer: Modifier = Modifier) {
    var user_mail by remember { mutableStateOf("") }
    var user_pwd by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.padding(30.dp).fillMaxSize().padding(top = 120.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "登 录",
            modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp),
            textAlign = TextAlign.Center,
            fontSize = 60.sp,
            fontFamily = FontFamily.SansSerif
        )


        TextField(
            modifier = Modifier.fillMaxWidth().height(80.dp),
            value = user_mail,
            onValueChange = { user_mail = it },
            label = {
                Text(
                    text = "账号",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            },
            placeholder = { Text("邮箱") },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.LightGray,
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray // 非聚焦时的指示器颜色
            )
        )


        TextField(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp).height(80.dp),
            value = user_pwd,
            onValueChange = { user_pwd = it },
            label = {
                Text(
                    text = "密码",
                    fontSize = 28.sp
                )
            },
            textStyle = TextStyle(fontSize = 25.sp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色
                unfocusedIndicatorColor = Color.Gray // 非聚焦时的指示器颜色
            )
        )
    }
}

@Preview
@Composable
fun LogInScreenPreview() {
    LogInScreenLayout()
}